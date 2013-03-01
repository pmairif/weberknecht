/*
 * Controller.java
 *
 * Copyright 2008-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.conf.ConfigurationException;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.db.DbConnectionHolder;
import de.highbyte_le.weberknecht.db.DbConnectionProvider;
import de.highbyte_le.weberknecht.request.actions.ActionNotFoundException;
import de.highbyte_le.weberknecht.request.routing.Router;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * webapp controller implemented as servlet
 * 
 * @author pmairif
 */
@SuppressWarnings({ "nls", "serial" })
public class Controller extends HttpServlet {
	
	private ControllerCore core;

	/**
	 * Logger for this class
	 */
	private final Log log = LogFactory.getLog(Controller.class);

	/**
	 * initialization of the controller
	 */
	@Override
	public void init() throws ServletException {
		try {
			core = new ControllerCore(getServletContext());
		}
		catch (Exception e) {
			log.error("init() - Exception: "+e.getMessage(), e);
			throw new ServletException("internal error", e);
		}
	}

	protected void init(ServletContext servletContext, WeberknechtConf conf, DbConnectionProvider dbConnectionProvider) throws ClassNotFoundException, ConfigurationException {
		core = new ControllerCore(servletContext, conf, dbConnectionProvider);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (log.isDebugEnabled())
			log.debug("service() - start");
		
		long start = System.currentTimeMillis();
		
		DbConnectionHolder conHolder = new DbConnectionHolder(core.getDbConnectionProvider());
		try {
			Router router = core.createRouter(conHolder);	//choose router depending on config
			RoutingTarget routingTarget = router.routeUri(request);

			core.executeAction(request, response, conHolder, routingTarget);
		}
		catch (ActionNotFoundException e1) {
			try {
				log.error("service() - exception while error handler instantiation: "+e1.getMessage(), e1);	//$NON-NLS-1$
				response.sendError(HttpServletResponse.SC_NOT_FOUND);	//call error page 404
			}
			catch (IOException e) {
				log.error("service() - IOException: "+e.getMessage(), e);	//$NON-NLS-1$
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);	//just return 404
			}
		}
		catch (Exception e1) {
			try {
				log.error("service() - exception while error handler instantiation: "+e1.getMessage(), e1);	//$NON-NLS-1$
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//call error page 500
			}
			catch (IOException e) {
				log.error("service() - IOException: "+e.getMessage(), e);	//$NON-NLS-1$
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//just return 500
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
		
		long finish = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("service() - page delivery took "+(finish-start)+" ms");
		}
	}
}
