/*
 * ActionViewProcessor.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 2010-11.21
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.ErrorHandler;

/**
 * process action views
 * 
 * @author pmairif
 */
public interface ActionViewProcessor {
	/**
	 * process action view
	 */
	public void processView(HttpServletRequest request, HttpServletResponse response, ExecutableAction action)
			throws ServletException, IOException, ContentProcessingException, ActionExecutionException;

	/**
	 * process error handler view
	 */
	public void processView(HttpServletRequest request, HttpServletResponse response, ErrorHandler errorHandler);
	
	public void setServletContext(ServletContext servletContext);
}
