/*
 * AreaCapableRouter.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 2011-12-22
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;

/**
 * Routing capable of handling areas.
 *
 * @author pmairif
 */
public class AreaCapableRouter implements Router {
	
	private static final Pattern pattern = Pattern.compile(
			"([/a-z0-9_-]+)?/([a-z0-9_-]+)(![a-z0-9_-]*)?\\.([a-z]+)",	//$NON-NLS-1$
			Pattern.CASE_INSENSITIVE
	); 

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#routeUri(java.lang.String)
	 */
	@Override
	public RoutingTarget routeUri(String servletPath) {
		RoutingTarget target = null;
		
		//TODO default action per area
		
		Matcher areaMatcher = pattern.matcher(servletPath);
		if (areaMatcher.matches()) {
			String path = areaMatcher.group(1);
			String baseName = areaMatcher.group(2);
			String t = areaMatcher.group(3);
			String suffix = areaMatcher.group(4);
			
			AreaPath areaPath = createPath(path);
			
			String task = null;
			if (t != null && t.length() > 1)
				task = t.substring(1);
			
			target = new RoutingTarget(areaPath, baseName, suffix, task);
		}
		
		return target;
	}

	protected AreaPath createPath(String path) {
		AreaPath ret = new AreaPath();
		
		if (path == null)
			return ret;
		
		String[] splitted = path.split("/");
		for (String s: splitted)
			ret.addPath(s);
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#setConfig(de.highbyte_le.weberknecht.conf.WeberknechtConf)
	 */
	@Override
	public void setConfig(WeberknechtConf conf) {
		// TODO Auto-generated method stub
		
	}
}
