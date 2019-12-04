/*
 * FlexRouter.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing.flex;

import com.github.pmairif.weberknecht.request.routing.AreaPathResolver;
import com.github.pmairif.weberknecht.request.routing.LocalePath;
import com.github.pmairif.weberknecht.request.routing.LocalePathResolver;
import com.github.pmairif.weberknecht.request.routing.Router;
import com.github.pmairif.weberknecht.request.routing.RoutingNotPossibleException;
import com.github.pmairif.weberknecht.request.routing.RoutingTarget;
import com.github.pmairif.weberknecht.conf.ConfigurationException;
import com.github.pmairif.weberknecht.conf.WeberknechtConf;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Routing delegated to routes. Supporting locale prefixes.
 *
 * @author pmairif
 */
public class FlexRouter implements Router {

	private WeberknechtConf conf = null;

    private final RouteCollection routes;

    private final String requestAttributePrefix;

    /**
     * @param routes  handles routes
     * @param requestAttributePrefix    prefix added when setting request attributes
     */
    public FlexRouter(RouteCollection routes, String requestAttributePrefix) {
        this.routes = routes;
        this.requestAttributePrefix = requestAttributePrefix;
    }

    @Override
	public RoutingTarget routeUri(HttpServletRequest request) {
		StringBuilder b = new StringBuilder(request.getServletPath());
		
		String pathInfo = request.getPathInfo();
		if (pathInfo != null)
			b.append(pathInfo);

        try {
            LocalePath localePath = new LocalePathResolver(conf).createPath(b.toString());

            Route route = routes.selectRoute(localePath.getPath());
            if (route == null)  //no route matches
                return null;

            Map<String, Object> parameterValues = route.getParameterValues();
            for (Map.Entry<String, Object> e: parameterValues.entrySet()) {
                request.setAttribute(requestAttributePrefix+"."+e.getKey(), e.getValue());
            }

            return new RoutingTarget(route, localePath.getLocale());
        }
        catch (RoutingNotPossibleException e) {
            //we cannot route, just return null
            return null;
        }
	}
	
	/* (non-Javadoc)
	 * @see Router#setConfig(WeberknechtConf)
	 */
	@Override
	public void setConfig(WeberknechtConf conf, AreaPathResolver pathResolver) {
		this.conf = conf;
	}
	
	public void setConfig(WeberknechtConf conf) throws ConfigurationException {
		setConfig(conf, null);
	}
}
