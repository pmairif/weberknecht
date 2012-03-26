/*
 * Router.java (weberknecht)
 *
 * Copyright 2011 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

/**
 * Interface for URI routers.
 *
 * <p>Implementations must be thread safe.</p>
 * 
 * @author pmairif
 */
public interface Router {
	//TODO app state (login etc)
	
	/**
	 * @return
	 * 		routing target information or null, if the URI didn't match
	 */
	public RoutingTarget routeUri(String servletPath);
}
