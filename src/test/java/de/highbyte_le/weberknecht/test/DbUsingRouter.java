/*
 * DbUsingRouter.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.test;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.request.DatabaseCapable;
import de.highbyte_le.weberknecht.request.routing.AreaPathResolver;
import de.highbyte_le.weberknecht.request.routing.Router;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * @author pmairif
 */
public class DbUsingRouter implements Router, DatabaseCapable {

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.DatabaseCapable#setDatabase(java.sql.Connection)
	 */
	@Override
	public void setDatabase(Connection con) {
		//
	}

	@Override
	public RoutingTarget routeUri(HttpServletRequest request) {
		return routeUri(request.getServletPath(), request.getPathInfo());
	}
	
	public RoutingTarget routeUri(String servletPath, String pathInfo) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#setConfig(de.highbyte_le.weberknecht.conf.WeberknechtConf)
	 */
	@Override
	public void setConfig(WeberknechtConf conf, AreaPathResolver pathResolver) {
		//
	}

}
