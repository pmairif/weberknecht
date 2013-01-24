/*
 * SimpleRouter.java (weberknecht)
 *
 * Copyright 2011 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 09.12.2011
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;

/**
 * Implementation of the Weberknecht default routing.
 *
 * @author pmairif
 */
public class SimpleRouter implements Router {
	
	private static final Pattern contextPathActionPattern = Pattern.compile("/([a-z0-9_-]+)(![a-z0-9_-]*)?\\.([a-z]+)"); //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#routeUri(java.lang.String)
	 */
	@Override
	public RoutingTarget routeUri(String servletPath) {
		RoutingTarget target = null;
		
		Matcher m = contextPathActionPattern.matcher(servletPath);
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
		
		return target;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#setConfig(de.highbyte_le.weberknecht.conf.WeberknechtConf)
	 */
	@Override
	public void setConfig(WeberknechtConf conf) {
		//
	}
}
