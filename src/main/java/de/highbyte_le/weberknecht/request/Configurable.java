/*
 * Configurable.java (weberknecht)
 *
 * Copyright 2009-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

import javax.servlet.ServletContext;

/**
 * Configurable via web.xml
 * 
 * @author pmairif
 */
public interface Configurable {
	/**
	 * passing servlet context.
	 * 
	 * @param context
	 */
	public void setContext(ServletContext context);
}
