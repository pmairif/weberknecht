/*
 * DbUsingRouter.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.test;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import com.github.pmairif.weberknecht.request.DatabaseCapable;
import com.github.pmairif.weberknecht.request.routing.AreaPathResolver;
import com.github.pmairif.weberknecht.request.routing.Router;
import com.github.pmairif.weberknecht.request.routing.RoutingTarget;
import com.github.pmairif.weberknecht.conf.WeberknechtConf;

/**
 * @author pmairif
 */
public class DbUsingRouter implements Router, DatabaseCapable {

	/* (non-Javadoc)
	 * @see DatabaseCapable#setDatabase(java.sql.Connection)
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
	 * @see Router#setConfig(WeberknechtConf)
	 */
	@Override
	public void setConfig(WeberknechtConf conf, AreaPathResolver pathResolver) {
		//
	}

}
