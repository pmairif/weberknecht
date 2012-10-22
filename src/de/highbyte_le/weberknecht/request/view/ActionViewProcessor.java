/*
 * ActionViewProcessor.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.view;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.Executable;
import de.highbyte_le.weberknecht.request.actions.ActionExecutionException;

/**
 * process action views
 * 
 * @author pmairif
 */
public interface ActionViewProcessor {
	/**
	 * process action view
	 */
	public void processView(HttpServletRequest request, HttpServletResponse response, Executable action)
			throws ServletException, IOException, ContentProcessingException, ActionExecutionException;

	public void setServletContext(ServletContext servletContext);
	
	public void setActionViewProcessorFactory(ActionViewProcessorFactory factory);
}
