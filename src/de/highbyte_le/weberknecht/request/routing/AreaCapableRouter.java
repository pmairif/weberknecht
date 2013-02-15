/*
 * AreaCapableRouter.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
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
	
	/**
	 * pattern for the whole URI
	 */
	private static final Pattern pattern = Pattern.compile(
			"([/a-z0-9_-]+)?/([a-z0-9_!-]+\\.[a-z]+)",	//$NON-NLS-1$
			Pattern.CASE_INSENSITIVE
	); 

	/**
	 * pattern for the action part
	 */
	private static final Pattern actionPattern = Pattern.compile(
			"([a-z0-9_-]+)(![a-z0-9_-]*)?\\.([a-z]+)",	//$NON-NLS-1$
			Pattern.CASE_INSENSITIVE
	);
	
	/**
	 * pattern for the path part
	 */
	private static final Pattern pathPattern = Pattern.compile("[/a-z0-9_-]+", Pattern.CASE_INSENSITIVE); 	//$NON-NLS-1$
	
	private WeberknechtConf conf = null;
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#routeUri(java.lang.String)
	 */
	@Override
	public RoutingTarget routeUri(String servletPath, String pathInfo) {
		StringBuilder b = new StringBuilder(servletPath);
		if (pathInfo != null)
			b.append(pathInfo);
		String combinedPath = b.toString();
		
		RoutingTarget target = null;
		
		Matcher areaMatcher = pattern.matcher(combinedPath);
		if (areaMatcher.matches()) {
			String path = areaMatcher.group(1);
			String action = areaMatcher.group(2);
			
			AreaPath areaPath = createPath(path);
			target = createTarget(areaPath, action);
		}
		else {	//check default action
			Matcher m = pathPattern.matcher(combinedPath);
			if (m.matches()) {
				AreaPath areaPath = createPath(combinedPath);
				
				String defaultAction = conf.getDefaultAction(areaPath);
				if (defaultAction != null)
					target = createTarget(areaPath, defaultAction);
			}
		}
		
		return target;
	}
	
	protected RoutingTarget createTarget(AreaPath areaPath, String actionString) {
		RoutingTarget target = null;

		Matcher m = actionPattern.matcher(actionString);
		if (m.matches()) {
			String baseName = m.group(1);
			String t = m.group(2);
			String suffix = m.group(3);

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
		this.conf = conf;
	}
}
