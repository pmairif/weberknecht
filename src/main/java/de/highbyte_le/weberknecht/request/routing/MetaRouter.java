/*
 * MetaRouter.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;

/**
 * iterates over a list of routers. the first router that matches, finishes processing and returns the result.
 *
 * @author pmairif
 */
public class MetaRouter implements Router {

	private List<Router> routers;
	
	public MetaRouter(List<Router> routers) {
		this.routers = routers;
	}

	public MetaRouter() {
		this.routers = new Vector<Router>();
	}
	
	@Override
	public RoutingTarget routeUri(HttpServletRequest request) {
		RoutingTarget target = null;
		
		for (Router r: routers) {
			target = r.routeUri(request);
			if (target != null)
				break;
		}
		
		return target;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#setConfig(de.highbyte_le.weberknecht.conf.WeberknechtConf)
	 */
	@Override
	public void setConfig(WeberknechtConf conf, AreaPathResolver pathResolver) {
		for (Router r: routers)
			r.setConfig(conf, pathResolver);
	}

	public void addRouter(Router router) {
		this.routers.add(router);
	}
	
	/**
	 * @return the routers
	 */
	public List<Router> getRouters() {
		return routers;
	}
}
