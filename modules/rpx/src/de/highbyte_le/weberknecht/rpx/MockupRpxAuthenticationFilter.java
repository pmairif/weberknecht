/*
 * MockupRpxAuthenticationFilter.java
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.rpx;

import java.io.IOException;

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
 * Mockup authenitication filter, that is API compatible with {@link RpxAuthenticationFilter}.
 * 
 * <p>It is useful for offline development. It is not possible, to simple replace the filter and click on the usual RPX sign in link,
 * but you can call the RPX response link manually (?do=rpx) and voilà, your signed in.
 * </p>
 * 
 * <p>URL parameter 'do' is used to control the processing. following values are recognized:
 * 	<ul>
 * 		<li>rpx: process RPX response to authenticate the user</li>
 * 		<li>signoff: remove authentication information from current session</li>
 * 	</ul>
 * </p>
 */
public class MockupRpxAuthenticationFilter implements Filter {

	private Class<?> persistenceMgrClass = null;
	
	private String signinUserGlobalId = null;
	
	/**
	 * Logger for this class
	 */
	private final Log logger = LogFactory.getLog(MockupRpxAuthenticationFilter.class);

	/**
	 * Called when Filter is put into service.
	 */
	@Override
	public void init(FilterConfig config) {
		try {
			//persistence manager class from config
			String persistenceManagerClass = config.getInitParameter("persistence_manager");
			this.signinUserGlobalId = config.getInitParameter("signin_user_globalid");
			if (persistenceManagerClass == null)
				logger.error("init() - persistence_manager is not set!");
			else {
				//Der Persistenzmanager sollte sich im Default-Constructor die Datenbankvebindung selbst beschaffen (zB JNDI)
				this.persistenceMgrClass = Class.forName(persistenceManagerClass);
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
				
				//process only RPX-Requests. we want the parameters untouched on usual post requests to be able to access the request content,
				String query = httpRequest.getQueryString();
				if (query != null && (query.contains("do=rpx") || query.contains("do=signoff"))) {
					String paramDo = request.getParameter("do");
					if (paramDo != null && paramDo.equalsIgnoreCase("rpx"))  {
						logger.debug("handle authentication request");
						handleAuthentication(httpRequest.getSession());
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
			throw new ServletException("Illegal access exception: "+e.getMessage(), e);
		}
	}
	
	protected void signOff(HttpSession session) {
		session.removeAttribute("user_auth");
		GeneralUserBean.clearSession(session);
	}

	/**
	 * do authentication, if parameters are present and check session for valid login
	 */
	protected void handleAuthentication(HttpSession session) throws DataAccessException, InstantiationException, IllegalAccessException, ServletException {
		//if right parameters are present, process the response
		session.removeAttribute("user_auth");	//first remove the previous login from session
		GeneralUserBean.clearSession(session);
		
		
		Object o = this.persistenceMgrClass.newInstance();
		if (!(o instanceof VisitorPersistenceManager))
			throw new ServletException("invalid persistence manager");
		
		VisitorPersistenceManager persistenceManager = (VisitorPersistenceManager) o;

		//store user in database or find existing user
		int userId = persistenceManager.getVisitorId(signinUserGlobalId);
		if (0 == userId)
			userId = persistenceManager.storeVisitor(signinUserGlobalId, null, null, null, null);

		GeneralUserBean userBean = persistenceManager.fetchVisitor(userId);
		userBean.storeInSession(session);
		
		//set authentication
		session.setAttribute("user_auth", new UserAuthentication(new Integer(userId), true));
	}
}
