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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Configurable via web.xml
 * 
 * @author pmairif
 */
public interface Configurable {
	public void setContext(ServletConfig config, ServletContext context);
}
