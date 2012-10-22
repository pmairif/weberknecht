/*
 * DataView.java
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.view;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.actions.ActionExecutionException;

/**
 * (actions) creating binary data
 * 
 * @author pmairif
 */
public interface DataView {
	
	/**
	 * <p>set the mime type with {@link HttpServletResponse#setContentType(String)}
	 * and write data to output stream with {@link HttpServletResponse#getOutputStream()}.</p>
	 */
	public void writeData(HttpServletResponse response) throws IOException, ActionExecutionException;
}
