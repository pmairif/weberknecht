/*
 * DataAction2.java
 *
 * Copyright 2009-2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.DatabaseCapable;

/**
 * action creating binary data
 * 
 * @author pmairif
 */
public interface DataAction2 extends DatabaseCapable, ExecutableAction {
	
	/**
	 * <p>set the mime type with {@link HttpServletResponse#setContentType(String)}
	 * and write data to output stream with {@link HttpServletResponse#getOutputStream()}.</p>
	 */
	public void writeData(HttpServletResponse response) throws IOException;
}
