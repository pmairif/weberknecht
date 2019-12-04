/*
 * LocalePrefixRouter.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.github.pmairif.weberknecht.conf.ConfigurationException;
import com.github.pmairif.weberknecht.conf.WeberknechtConf;

/**
 * Routing capable of handling areas and supporting locale prefixes.
 *
 * @author pmairif
 */
public class LocalePrefixRouter implements Router {

	/**
	 * pattern for the action part
	 */
	private static final Pattern actionPattern = Pattern.compile(
			"/?([a-z0-9_-]+)(![a-z0-9_-]*)?(\\.[a-z]+)?",	//$NON-NLS-1$
			Pattern.CASE_INSENSITIVE
	);
	
	private WeberknechtConf conf = null;
	
	private AreaPathResolver pathResolver = null;

	@Override
	public RoutingTarget routeUri(HttpServletRequest request) {
		StringBuilder b = new StringBuilder(request.getServletPath());
		
		String pathInfo = request.getPathInfo();
		if (pathInfo != null)
			b.append(pathInfo);

		return process(b.toString());
	}
	
	public RoutingTarget process(String path) {
		try {
			RoutingTarget target = null;

			LocalePath localePath = new LocalePathResolver(conf).createPath(path);
			List<String> areas = localePath.getPath().getAreas();
			int areaSize = areas.size();
			
			//1.) last part interpreted as action
			if (areaSize > 0) {
				String action =  areas.get(areaSize-1);
				List<String> subPath = areas.subList(0, areaSize-1);
				target = createTarget(localePath.getLocale(), subPath, action);

				if (pathResolver.knownTarget(target))
					return target;
			}

			//2.) path with default action
			String defaultAction = conf.getDefaultAction(localePath.getPath());
			if (defaultAction != null) {
				target = createTarget(localePath.getLocale(), areas, defaultAction);

				if (pathResolver.knownTarget(target))
					return target;
			}

			return null;
		}
		catch (RoutingNotPossibleException e) {
			//we cannot route, just return null
			return null;
		}
	}
	
	protected RoutingTarget createTarget(Locale locale, List<String> areas, String actionString) {
		RoutingTarget target = null;

		Matcher m = actionPattern.matcher(actionString);
		if (m.matches()) {
			String baseName = m.group(1);
			String t = m.group(2);
			String s = m.group(3);

			String task = null;
			if (t != null && t.length() > 1)
				task = t.substring(1);
			
			String suffix = "";
			if (s != null && s.length() > 1)
				suffix = s.substring(1);
			
			target = new RoutingTarget(new AreaPath(areas), baseName, suffix, task, locale);
		}
		
		return target;
	}

	/* (non-Javadoc)
	 * @see Router#setConfig(WeberknechtConf)
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
