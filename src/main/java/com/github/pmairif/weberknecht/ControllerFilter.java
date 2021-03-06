/*
 * ControllerFilter.java
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pmairif.weberknecht.conf.WeberknechtConf;
import com.github.pmairif.weberknecht.request.routing.Router;
import com.github.pmairif.weberknecht.request.routing.RoutingTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pmairif.weberknecht.conf.ConfigurationException;
import com.github.pmairif.weberknecht.db.DbConnectionHolder;
import com.github.pmairif.weberknecht.db.DbConnectionProvider;

/**
 * webapp controller using servlet filter mechanism
 * 
 * @author pmairif
 */
@SuppressWarnings({ "nls" })
public class ControllerFilter implements Filter {
	
	private ControllerCore core;
	
	/**
	 * Logger for this class
	 */
	private final Logger log = LoggerFactory.getLogger(ControllerFilter.class);

	/**
	 * filter initialization
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			core = new ControllerCore(filterConfig.getServletContext());
		}
		catch (Exception e) {
			log.error("init() - Exception: "+e.getMessage(), e);
			throw new ServletException("internal error", e);
		}
	}
	
	protected void init(ServletContext servletContext, WeberknechtConf conf, DbConnectionProvider dbConnectionProvider) throws ClassNotFoundException, ConfigurationException {
		core = new ControllerCore(servletContext, conf, dbConnectionProvider);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		if (log.isDebugEnabled())
			log.debug("doFilter() - start");
		
		long start = System.currentTimeMillis();
		
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
			throw new IllegalArgumentException("no http request");
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		DbConnectionHolder conHolder = new DbConnectionHolder(core.getDbConnectionProvider());
		try {
			Router router = core.createRouter(conHolder);	//choose router depending on config
			RoutingTarget routingTarget = router.routeUri(httpRequest);
			if (null == routingTarget) {
				filterChain.doFilter(request, response);
			}
			else {
				core.executeAction(httpRequest, httpResponse, conHolder, routingTarget);

				long finish = System.currentTimeMillis();
				if (log.isInfoEnabled()) {
					log.info("service() - page delivery of '"+httpRequest.getRequestURI()+"' took "+(finish-start)+" ms");
				}
			}
		}
		catch (Exception e1) {
			try {
				log.error("service() - exception while error handler instantiation: "+e1.getMessage(), e1);	//$NON-NLS-1$

				//exceptions occurring here were not processed by weberknecht error handlers.
				//so it is probably a good idea to go on with the error handling of the container. 
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//call error page 500
			}
			catch (IOException e) {
				log.error("service() - IOException: "+e.getMessage(), e);	//$NON-NLS-1$
				httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//just return 500
			}
		}
		finally {
			try {
				conHolder.close();
			}
			catch (SQLException e) {
				log.error("service() - SQLException while closing db connection: "+e.getMessage());	//$NON-NLS-1$
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		//
	}
}
