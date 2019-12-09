/*
 * AuthenticationFilter.java
 *
 * Copyright 2008 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.security.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pmairif.weberknecht.security.UserAuthentication;
import com.github.pmairif.weberknecht.security.UsernamePasswordAuthenticator;

/**
 * Filter to handle user authentication
 * 
 * <p>
 * 	User authentication by user name and password is validated. Session properties are set accordingly.
 * 	No forwarding to login-page or sth. will be done.
 * </p>
 * 
 * <p>
 * 	The do parameter with value "auth" has to be passed via query string, no matter if you call GET or POST.
 * </p>
 */
@SuppressWarnings("nls")
public class AuthenticationFilter implements Filter {
	private UsernamePasswordAuthenticator authentication = null;

	/**
	 * URL parameter checked for "auth"-value. Default is "do".
	 */
	private String doParameter = null;
	
	/**
	 * Logger for this class
	 */
	private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	/**
	 * Called when Filter is put into service.
	 */
	@Override
	public void init(FilterConfig config) {
		String authenticationClass = config.getInitParameter("authentication_class");
		if (authenticationClass == null)
			logger.error("init() - authentication_class is not set!");
		else {
			try {
				Object o = Class.forName(authenticationClass).newInstance();
				if (o instanceof UsernamePasswordAuthenticator) {
					authentication = (UsernamePasswordAuthenticator) o;
				}
				else {
					logger.error("init() - Authenticator class is not an instance of UsernamePasswordAuthenticator");
				}
			}
			catch (Exception e) {
				logger.error("init() - authentication class couldn't be initialized");
			}
		}
		
		doParameter = config.getInitParameter("do_param");
		if (null == doParameter)
			doParameter = "do";
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
		if (logger.isDebugEnabled())
			logger.debug("doFilter() - start");

		if (!(request instanceof HttpServletRequest)) {
			//no HttpServletRequest, nothing we can do
			logger.error("doFilter() - servlet request is no HTTP servlet request");
		}
		else {
			handleAuthentication((HttpServletRequest) request);

			logger.debug("doFilter() - Continue with filter chain");
			chain.doFilter(request, response);
		}
	}

	/**
	 * do authentication, if parameters are present and check session for valid login
	 * @return true, if the user is authenticated
	 */
	protected boolean handleAuthentication(HttpServletRequest request) {
		HttpSession session = request.getSession();

		String reqUser = null;
		String reqPwd = null;
		String reqDoParam = null;

		//Erst prüfen, ob Authentifizierungsversuch vorliegt, da es nach getParameter() nicht mehr möglich ist auf gepostete formulare zuzugreifen.
		String queryString = request.getQueryString();
		if (queryString != null && (queryString.contains(doParameter+"=auth") || queryString.contains(doParameter+"=signout")) ) {
			// Get user id and password from request parameters
			reqUser = request.getParameter("user");		
			reqPwd = request.getParameter("pwd");
			reqDoParam = request.getParameter(doParameter);
		}
		
		boolean success = false;

		// If user id and password are present, authenticate
		if (reqUser != null && reqPwd != null && reqDoParam != null && reqDoParam.equalsIgnoreCase("auth")) {
			//new session on login to prevent session fixation attacks
			logger.info("login attempt - create new session");
			session.invalidate();
			session = request.getSession(true);

			if (authentication != null) {
				Integer userId = authentication.authenticate(reqUser, reqPwd);
				if (userId != null) {	//successful login
					session.setAttribute("user_auth", new UserAuthentication(userId, true));
					success = true;
				}
			}
			else {
				logger.error("handleAuthentication() - no Authenticator object set!");
			}
		}
		else if (reqDoParam != null && reqDoParam.equalsIgnoreCase("signout")) {
			session.removeAttribute("user_auth");	//remove the previous login from session
		}
		else {			//use login information from session
			UserAuthentication userAuth = (UserAuthentication) session.getAttribute("user_auth");
			if (userAuth != null) {
				success = userAuth.isAuthenticated();
			}
		}
				
		return success;
	}
}
