/*
 * AreaCapableRouter.java (weberknecht)
 *
 * Copyright 2011 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 2011-12-22
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Routing capable of handling areas.
 *
 * @author pmairif
 */
public class AreaCapableRouter implements Router {
	
	private static final Pattern pattern = Pattern.compile("/([a-z0-9_-]+/)?([a-z0-9_-]+)(![a-z0-9_-]*)?\\.([a-z]+)", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#routeUri(java.lang.String)
	 */
	@Override
	public RoutingTarget routeUri(String servletPath) {
		RoutingTarget target = null;
		
		//TODO default action/area
		
		Matcher areaMatcher = pattern.matcher(servletPath);
		if (areaMatcher.matches()) {
			String a = areaMatcher.group(1);
			String baseName = areaMatcher.group(2);
			String t = areaMatcher.group(3);
			String suffix = areaMatcher.group(4);
			
			String area = null;
			if (a != null && a.length() > 1)
				area = a.substring(0, a.length()-1);
			
			String task = null;
			if (t != null && t.length() > 1)
				task = t.substring(1);
			
			target = new RoutingTarget(area, baseName, suffix, task);
		}
		
		return target;
	}

}
