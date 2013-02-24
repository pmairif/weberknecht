/*
 * LocalePrefixRouter.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.highbyte_le.weberknecht.conf.ConfigurationException;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;

/**
 * Routing capable of handling areas and supporting locale prefixes.
 *
 * @author pmairif
 */
public class LocalePrefixRouter implements Router {

	/**
	 * pattern for the whole URI
	 */
	private static final Pattern pattern = Pattern.compile(
			"([/a-z0-9_-]+)?(/[a-z0-9_!-]+\\.[a-z]+)?",	//$NON-NLS-1$
			Pattern.CASE_INSENSITIVE
	); 

	/**
	 * pattern for the action part
	 */
	private static final Pattern actionPattern = Pattern.compile(
			"/?([a-z0-9_-]+)(![a-z0-9_-]*)?\\.([a-z]+)",	//$NON-NLS-1$
			Pattern.CASE_INSENSITIVE
	);
	
	private WeberknechtConf conf = null;
	
	private AreaPathResolver pathResolver = null;

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#routeUri(java.lang.String)
	 */
	@Override
	public RoutingTarget routeUri(String servletPath, String pathInfo) {
		try {
			RoutingTarget target = null;

			StringBuilder b = new StringBuilder(servletPath);
			if (pathInfo != null)
				b.append(pathInfo);
			
			Matcher areaMatcher = pattern.matcher(b.toString());
			if (areaMatcher.matches()) {
				String path = areaMatcher.group(1);
				String action = areaMatcher.group(2);
				
				LocalePath localePath = new LocalePathResolver(conf).createPath(path);
				if (action != null && action.length() > 0) {
					target = createTarget(localePath, action);
				}
				else {
					String defaultAction = conf.getDefaultAction(localePath.getPath());
					if (defaultAction != null)
						target = createTarget(localePath, defaultAction);
				}

			}

			if (pathResolver.knownTarget(target))
				return target;
			return null;
		}
		catch (RoutingNotPossibleException e) {
			//we cannot route, just return null
			return null;
		}
	}
	
	protected RoutingTarget createTarget(LocalePath localePath, String actionString) {
		RoutingTarget target = null;

		Matcher m = actionPattern.matcher(actionString);
		if (m.matches()) {
			String baseName = m.group(1);
			String t = m.group(2);
			String suffix = m.group(3);

			String task = null;
			if (t != null && t.length() > 1)
				task = t.substring(1);
			
			target = new RoutingTarget(localePath.getPath(), baseName, suffix, task, localePath.getLocale());
		}
		
		return target;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.routing.Router#setConfig(de.highbyte_le.weberknecht.conf.WeberknechtConf)
	 */
	@Override
	public void setConfig(WeberknechtConf conf, AreaPathResolver pathResolver) {
		this.conf = conf;
		this.pathResolver = pathResolver;
	}
	
	/**
	 * set config and create new instance of {@link AreaPathResolver}.
	 */
	public void setConfig(WeberknechtConf conf) throws ConfigurationException {
		setConfig(conf, new AreaPathResolver(conf));
	}
	
}
