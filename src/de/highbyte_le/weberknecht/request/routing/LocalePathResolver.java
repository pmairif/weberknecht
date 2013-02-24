/*
 * LocalePathResolver.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.highbyte_le.weberknecht.conf.RoutingLocalePrefix;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;

/**
 * extract locale from path
 *
 * @author pmairif
 */
public class LocalePathResolver {
	private static final Pattern localePattern = Pattern.compile("([a-z]{2})(?:_([A-Z]{2}))?");
	
	private final WeberknechtConf conf;
	
	public LocalePathResolver(WeberknechtConf conf) {
		this.conf = conf;
	}

	public LocalePath createPath(String path) throws RoutingNotPossibleException {
		if (path == null)
			return new LocalePath();
			
		String[] pathSegments = path.split("/");
		pathSegments = ltrim(pathSegments);
		return extractLocale(pathSegments);
	}
	
	/**
	 * remove empty segments from the left
	 */
	protected String[] ltrim(String[] pathSegments) {
		if (null == pathSegments)
			return null;
		
		int j=0;
		int length = pathSegments.length;
		while (length > j && pathSegments[j].length() == 0)
			j++;
		
		String[] ret = new String[length-j];
		for (int i=j; i<length; i++)
			ret[i-1] = pathSegments[i];
		
		return ret;
	}

	protected LocalePath extractLocale(String[] pathSegments) throws RoutingNotPossibleException {
		LocalePath ret = null;
		
		RoutingLocalePrefix prefix = conf.getRoutingLocalePrefix();
		if (null != prefix)	{	//no prefix, no handling necessary
			if (null == pathSegments || pathSegments.length == 0) {
				if (!prefix.isOptional())
					throw new RoutingNotPossibleException();
			}
			else {
				String candidate = pathSegments[0];
				if (prefix.getAllowedLocales().contains(candidate)) {
					Locale locale = parseLocale(candidate);
					
					AreaPath path = new AreaPath();
					for (int i=1; i<pathSegments.length; i++)
						path.addPath(pathSegments[i]);
					
					ret = new LocalePath(path, locale);
				}
				else if (!prefix.isOptional()) {
					throw new RoutingNotPossibleException();
				}
			}
		}
		
		if (null == ret)
			ret = new LocalePath(pathSegments, null);
		
		return ret;
	}
	
	protected Locale parseLocale(String candidate) {
		if (null == candidate)
			return null;

		String cand = candidate.trim();
		Locale locale = null;
		if (cand.length() > 0) {
			Matcher m = localePattern.matcher(cand);
			if (m.matches()) {
				String g1 = m.group(1);
				String g2 = m.group(2);
				if (g2 != null)
					locale = new Locale(g1, g2);
				else
					locale = new Locale(g1);
			}
		}
		return locale;
	}
}
