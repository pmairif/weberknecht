/*
 * Configurable.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 12.11.2009
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Configurable via web.xml
 * 
 * @author pmairif
 */
public interface Configurable {
	/**
	 * passing servlet or filter config.
	 * 
	 * Either servlet or filter config is set depending on the controller that is used.
	 * 
	 * @param servletConfig
	 * @param filterConfig
	 * @param context
	 */
	public void setContext(ServletConfig servletConfig, FilterConfig filterConfig, ServletContext context);
}
