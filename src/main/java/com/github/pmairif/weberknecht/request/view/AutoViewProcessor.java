/*
 * AutoViewProcessor.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
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

import com.github.pmairif.weberknecht.request.actions.ActionExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pmairif.weberknecht.request.ContentProcessingException;
import com.github.pmairif.weberknecht.request.Executable;

/**
 * Let the executable choose on its own which view processor to use.
 * 
 * @author pmairif
 */
public class AutoViewProcessor implements ActionViewProcessor {
	
	private final Logger log = LoggerFactory.getLogger(AutoViewProcessor.class);
	
	private ServletContext servletContext = null;
	
	private ActionViewProcessorFactory factory = null;
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionProcessor#processAction(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ExecutableAction)
	 */
	@Override
	public boolean processView(HttpServletRequest request, HttpServletResponse response, Executable executable)
			throws ServletException, IOException, ContentProcessingException, ActionExecutionException {
		
		if (log.isDebugEnabled())
			log.debug("processView() - processing action "+executable.getClass().getSimpleName());

		if (!(executable instanceof AutoView))
			throw new IllegalArgumentException("Executable not applicable here.");

		String viewProcessorName = ((AutoView) executable).getViewProcessorName();
		ActionViewProcessor processor = factory.createActionProcessor(viewProcessorName, servletContext);
		
		if (processor instanceof AutoViewProcessor)		//avoid endless loops
			throw new IllegalArgumentException("auto view recursivly requested");

		boolean view = false;
		if (null == processor) {
			log.info("processView() - no view processor for " + executable.getClass().getName());
		}
		else {
			processor.processView(request, response, executable);
			view = true;
		}
		
		return view;
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	/* (non-Javadoc)
	 * @see ActionViewProcessor#setActionViewProcessorFactory(ActionViewProcessorFactory)
	 */
	@Override
	public void setActionViewProcessorFactory(ActionViewProcessorFactory factory) {
		this.factory = factory;
	}
}
