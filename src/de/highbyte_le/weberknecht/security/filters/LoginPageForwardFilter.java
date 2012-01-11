/*
 * LoginPageForwardFilter.java
 *
 * Copyright 2008 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.security.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.security.UserAuthentication;

/**
 * forward to login page, if user is not authenticated.
 */
@SuppressWarnings("nls")
public class LoginPageForwardFilter implements Filter {
	@Override
	public void init(FilterConfig config) {
		loginPage = config.getInitParameter("login_page");
	}
	
	@Override
	public void destroy() {
		//
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (logger.isDebugEnabled())
			logger.debug("doFilter() - start");

		boolean forward = true;
		
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			logger.error("servlet request is no HTTP servlet request");
		}
		else {
			HttpSession session = ((HttpServletRequest)request).getSession();
			
			UserAuthentication userAuthentication = (UserAuthentication) session.getAttribute("user_auth");
			if (userAuthentication != null && userAuthentication.isAuthenticated())
				forward = false;
		}
		
		if (forward) {
			request.setAttribute("de.highbyte_le.weberknecht.login.status", "failed");
			
			logger.debug("doFilter() - forward to login page");
			RequestDispatcher rd = request.getRequestDispatcher(loginPage);
			rd.forward(request, response);
		}
		else {
			logger.debug("doFilter() - Continue with filter chain");
			chain.doFilter(request, response);
		}
	}
	
	private String loginPage = null;

	/**
	 * Logger for this class
	 */
	private final Log logger = LogFactory.getLog(LoginPageForwardFilter.class);
}
