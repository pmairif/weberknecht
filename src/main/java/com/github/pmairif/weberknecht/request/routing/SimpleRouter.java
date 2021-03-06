/*
 * SimpleRouter.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.github.pmairif.weberknecht.conf.ConfigurationException;
import com.github.pmairif.weberknecht.conf.WeberknechtConf;

/**
 * Implementation of the Weberknecht default routing.
 *
 * @author pmairif
 */
public class SimpleRouter implements Router {
	
	private static final Pattern contextPathActionPattern = Pattern.compile("/([a-z0-9_-]+)(![a-z0-9_-]*)?\\.([a-z]+)"); //$NON-NLS-1$

	private AreaPathResolver pathResolver = null;

	/* (non-Javadoc)
	 * @see Router#routeUri(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public RoutingTarget routeUri(HttpServletRequest request) {
		return routeUri(request.getServletPath(), request.getPathInfo());
	}
	
	public RoutingTarget routeUri(String servletPath, String pathInfo) {
		RoutingTarget target = null;
		
		StringBuilder b = new StringBuilder(servletPath);
		if (pathInfo != null)
			b.append(pathInfo);
		
		Matcher m = contextPathActionPattern.matcher(b.toString());
		if (m.matches()) {
			String area = null;				//simple router doesn't support areas
			String baseName = m.group(1);
			String suffix = m.group(3);
			String task = null;
			
			String t = m.group(2);
			if (t != null && t.length() > 1)
				task = t.substring(1);
			
			target = new RoutingTarget(area, baseName, suffix, task);
		}
		
		if (pathResolver.knownTarget(target))
			return target;
		return null;
	}

	/* (non-Javadoc)
	 * @see Router#setConfig(WeberknechtConf)
	 */
	@Override
	public void setConfig(WeberknechtConf conf, AreaPathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}
	
	/**
	 * set config and create new instance of {@link AreaPathResolver}.
	 */
	public void setConfig(WeberknechtConf conf) throws ConfigurationException {
		setConfig(conf, new AreaPathResolver(conf));
	}
}
