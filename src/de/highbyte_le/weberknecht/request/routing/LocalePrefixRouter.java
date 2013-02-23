/*
 * LocalePrefixRouter.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.highbyte_le.weberknecht.conf.ConfigurationException;
import de.highbyte_le.weberknecht.conf.RoutingLocalePrefix;
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
			"([/a-z0-9_-]+)?(/[a-z0-9_!.-]+)?",	//$NON-NLS-1$
			Pattern.CASE_INSENSITIVE
	); 

	/**
	 * pattern for the action part
	 */
	private static final Pattern actionPattern = Pattern.compile(
			"/?([a-z0-9_-]+)(![a-z0-9_-]*)?(\\.[a-z]+)?",	//$NON-NLS-1$
			Pattern.CASE_INSENSITIVE
	);
	
	private static final Pattern localePattern = Pattern.compile("([a-z]{2})(?:_([A-Z]{2}))?");
	
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
				
				LocalePathResolver resolver = createPath(path);
				AreaPath areaPath = resolver.getPath();
				Locale locale = resolver.getLocale();
				if (null == locale && conf.hasRoutingLocalePrefix() && !conf.getRoutingLocalePrefix().isOptional())
					throw new RoutingNotPossibleException();

				if (action != null && action.length() > 0) {
					target = createTarget(locale, areaPath, action);
				}
				else {
					String defaultAction = conf.getDefaultAction(areaPath);
					if (defaultAction != null)
						target = createTarget(locale, areaPath, defaultAction);
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
	
	public LocalePathResolver createPath(String path) throws RoutingNotPossibleException {
		LocalePathResolver resolver = new LocalePathResolver();

		if (path != null) {
			String[] pathSegments = path.split("/");
			pathSegments = resolver.ltrim(pathSegments);
			pathSegments = resolver.extractLocale(pathSegments);
			resolver.extractPath(pathSegments);
		}
		
		return resolver;
	}
	
	protected RoutingTarget createTarget(Locale locale, AreaPath areaPath, String actionString) {
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
			
			target = new RoutingTarget(areaPath, baseName, suffix, task, locale);
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
	
	class LocalePathResolver {
		private AreaPath path = new AreaPath();
		
		private Locale locale = null;

		/**
		 * remove empty segments from the left
		 */
		public String[] ltrim(String[] pathSegments) {
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

		public String[] extractLocale(String[] pathSegments) throws RoutingNotPossibleException {
			String[] ret = pathSegments;
			
			RoutingLocalePrefix prefix = conf.getRoutingLocalePrefix();
			if (null == prefix)	//no prefix, no handling necessary
				return ret;
			
			if (null == pathSegments || pathSegments.length == 0) {
				if (!prefix.isOptional())
					throw new RoutingNotPossibleException();
				return ret;
			}
			
			Set<String> allowed = prefix.getAllowedLocales();
			String candidate = pathSegments[0];
			if (allowed.contains(candidate)) {
				locale = parseLocale(candidate);
				
				ret = new String[pathSegments.length-1];
				for (int i=1; i<pathSegments.length; i++)
					ret[i-1] = pathSegments[i];
			}
			else {
				if (!prefix.isOptional())
					throw new RoutingNotPossibleException();
			}
			
			return ret;
		}
		
		public void extractPath(String[] pathSegments) {
			for (String s: pathSegments)
				this.path.addPath(s);
		}
		
		/**
		 * @return the path
		 */
		public AreaPath getPath() {
			return path;
		}
		
		/**
		 * @return the locale
		 */
		public Locale getLocale() {
			return locale;
		}
	}
}
