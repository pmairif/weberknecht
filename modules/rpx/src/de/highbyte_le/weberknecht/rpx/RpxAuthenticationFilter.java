/*
 * RpxAuthenticationFilter.java
 *
 * Copyright 2009-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.rpx;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.model.GeneralUserBean;
import de.highbyte_le.weberknecht.persistence.DataAccessException;
import de.highbyte_le.weberknecht.persistence.VisitorPersistenceManager;
import de.highbyte_le.weberknecht.security.UserAuthentication;

/**
 * Filter to handle RPX responses
 * 
 * <p>
 * RPX response is processed,
 * local user account is created or fetched,
 * session properties are set accordingly.
 * </p>
 * 
 * <p>URL parameter 'do' is used to control the processing. following values are recognized:
 * 	<ul>
 * 		<li>rpx: process RPX response to authenticate the user</li>
 * 		<li>signoff: remove authentication information from current session</li>
 * 	</ul>
 * </p>
 */
public class RpxAuthenticationFilter implements Filter {

	private Class<?> persistenceManagerClass = null;
	
	private Rpx rpx = null;

	/**
	 * Logger for this class
	 */
	private final Log logger = LogFactory.getLog(RpxAuthenticationFilter.class);

	/**
	 * Called when Filter is put into service.
	 */
	@Override
	public void init(FilterConfig config) {
		try {
			//persistence manager class from config
			String clazz = config.getInitParameter("persistence_manager");
			if (clazz == null)
				logger.error("init() - persistence_manager is not set!");
			else {
				try {
					persistenceManagerClass = Class.forName(clazz);
				}
				catch (Exception e) {
					logger.error("init() - persistence manager class couldn't be initialized");
				}
			}
			
			//RPX API Key via JNDI
			try {
	        	Context ctx = new InitialContext();
	            Context envCtx = (Context) ctx.lookup("java:comp/env"); //$NON-NLS-1$
	
	            String apiKey = (String) envCtx.lookup("rpx_api_key"); //$NON-NLS-1$
	            this.rpx = new Rpx(apiKey);
			}
	        catch (NamingException e) {
	            logger.error("init() - naming exception: "+e.getMessage()); //$NON-NLS-1$
	        }
		}
		catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Reset the Filter configuration.
	 */
	@Override
	public void destroy() {
		//
	}

	/**
	 * Execution code for the filter.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			if (logger.isDebugEnabled())
				logger.debug("doFilter() - start");
	
			if (!(request instanceof HttpServletRequest)) {
				//no HttpServletRequest, nothing we can do
				logger.error("doFilter() - servlet request is no HTTP servlet request");
			}
			else {
				HttpServletRequest httpRequest = (HttpServletRequest) request;

				if (null == persistenceManagerClass)
					throw new ServletException("persistence manager class couldn't be initialized");
				
				Object o = persistenceManagerClass.newInstance();
				if (!(o instanceof VisitorPersistenceManager))
					throw new ServletException("persistence manager class is not an instance of VisitorPersistenceManager");
				
				VisitorPersistenceManager persistenceManager = (VisitorPersistenceManager) o;

				//process only RPX-Requests. we want the parameters untouched on usual post requests to be able to access the request content,
				String query = httpRequest.getQueryString();
				if (query != null && (query.contains("do=rpx") || query.contains("do=signoff"))) {
					String paramDo = request.getParameter("do");
					String paramToken = request.getParameter("token");
					if (paramDo != null && paramDo.equalsIgnoreCase("rpx") && paramToken != null)  {
						logger.debug("handle authentication request");
						handleAuthentication(persistenceManager, httpRequest.getSession(), paramToken);
					}
					else if (paramDo != null && paramDo.equalsIgnoreCase("signoff")) {
						logger.debug("sign off");
						signOff(httpRequest.getSession());
					}
				}
	
				logger.debug("doFilter() - Continue with filter chain");
				chain.doFilter(request, response);
			}
		}
		catch (RpxException e) {
			logger.error("doFilter() - RpxException: "+e.getMessage(), e);	//$NON-NLS-1$
			throw new ServletException("RPX Exception: "+e.getMessage(), e);
		}
		catch (DataAccessException e) {
			logger.error("doFilter() - DataAccessException: "+e.getMessage(), e);	//$NON-NLS-1$
			throw new ServletException("data access Exception: "+e.getMessage(), e);
		}
		catch (InstantiationException e) {
			logger.error("doFilter() - InstantiationException: "+e.getMessage(), e);	//$NON-NLS-1$
			throw new ServletException("Instantiation Exception: "+e.getMessage(), e);
		}
		catch (IllegalAccessException e) {
			logger.error("doFilter() - IllegalAccessException: "+e.getMessage(), e);	//$NON-NLS-1$
			throw new ServletException("illegal access exception: "+e.getMessage(), e);
		}
	}
	
	protected void signOff(HttpSession session) {
		session.removeAttribute("user_auth");
		GeneralUserBean.clearSession(session);
	}

	/**
	 * do authentication, if parameters are present and check session for valid login
	 */
	protected void handleAuthentication(VisitorPersistenceManager persistenceManager, HttpSession session, String token) throws RpxException, DataAccessException {
		//if right parameters are present, process the response
		session.removeAttribute("user_auth");	//first remove the previous login from session
		GeneralUserBean.clearSession(session);
		
		RpxAuthInfo authInfo = rpx.authInfo(token);
		
		if (logger.isDebugEnabled()) {
			StringBuilder b = new StringBuilder();
			b.append("got following profile data:\n");
			for (String key: authInfo.getValues().keySet()) {
				b.append("\t").append(key).append(": ").append(authInfo.getValue(key)).append("\n");
			}
			logger.debug(b.toString());
		}
		
		GeneralUserBean userBean = new GeneralUserBean();
		userBean.populate(authInfo);
		userBean.storeInSession(session);
		
		//store user in database or find existing user
		if (persistenceManager != null) {
			int userId = persistenceManager.getVisitorId(userBean.getIdentifier());
			if (0 == userId)
				userId = persistenceManager.storeVisitor(userBean.getIdentifier(), userBean.getDisplayName(), userBean.getEmail(),
						userBean.getVerifiedEmail(), userBean.getUrl());
			
			userBean.setId(userId);
			
			//set authentication
			session.setAttribute("user_auth", new UserAuthentication(new Integer(userId), true));
		}
	}
}
