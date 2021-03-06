/*
 * ActionViewProcessor.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.view;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pmairif.weberknecht.request.ContentProcessingException;
import com.github.pmairif.weberknecht.request.actions.ActionExecutionException;
import com.github.pmairif.weberknecht.request.Executable;

/**
 * process action views
 * 
 * @author pmairif
 */
public interface ActionViewProcessor {
	/**
	 * process action view
	 * @return true, if there was a view to process. false, if not.
	 */
	boolean processView(HttpServletRequest request, HttpServletResponse response, Executable action)
			throws ServletException, IOException, ContentProcessingException, ActionExecutionException;

	void setServletContext(ServletContext servletContext);
	
	void setActionViewProcessorFactory(ActionViewProcessorFactory factory);
}
