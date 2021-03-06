/*
 * DataActionProcessor.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.view;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pmairif.weberknecht.request.actions.ActionExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pmairif.weberknecht.request.Executable;

/**
 * Process data views
 * @author pmairif
 */
public class DataActionProcessor implements ActionViewProcessor {
	
	private final Logger log = LoggerFactory.getLogger(DataActionProcessor.class);
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionProcessor#processAction(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ExecutableAction)
	 */
	@Override
	public boolean processView(HttpServletRequest request, HttpServletResponse response, Executable action) throws IOException, ActionExecutionException {
		if (action instanceof DataView)
			return processView(request, response, (DataView)action);
		
		throw new IllegalArgumentException("Action not applicable here.");
	}
	
	public boolean processView(HttpServletRequest request, HttpServletResponse response, DataView action)
			throws IOException, ActionExecutionException {
		
		if (log.isDebugEnabled())
			log.debug("processView() - processing action "+action.getClass().getSimpleName());
	
		//write output
		action.writeData(response);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionViewProcessor#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		//
	}
	
	@Override
	public void setActionViewProcessorFactory(ActionViewProcessorFactory factory) {
		//
	}
}
