/*
 * Router.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import javax.servlet.http.HttpServletRequest;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;

/**
 * Interface for URI routers.
 *
 * <p>Implementations must be thread safe.</p>
 * 
 * @author pmairif
 */
public interface Router {
	/**
	 * @return
	 * 		routing target information or null, if the URI didn't match
	 */
	public RoutingTarget routeUri(HttpServletRequest request);
	
	/**
	 * routers should be able to access the configuration and path resolver
	 */
	public void setConfig(WeberknechtConf conf, AreaPathResolver areaPathResolver);
}
