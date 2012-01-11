/*
 * ActionViewProcessor.java (weberknecht)
 *
 * Copyright 2010 Patrick Mairif.
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

/**
 * process action views
 * 
 * @author pmairif
 */
public interface ActionViewProcessor {
	public void processView(HttpServletRequest request, HttpServletResponse response, ExecutableAction action)
			throws ServletException, IOException, ContentProcessingException, ActionExecutionException;
	
	public void setServletContext(ServletContext servletContext);
}
