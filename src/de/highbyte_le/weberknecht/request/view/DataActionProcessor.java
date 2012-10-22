/*
 * DataActionProcessor.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.view;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.request.Executable;
import de.highbyte_le.weberknecht.request.actions.ActionExecutionException;

/**
 * Process data views
 * @author pmairif
 */
public class DataActionProcessor implements ActionViewProcessor {
	
	private final Log log = LogFactory.getLog(DataActionProcessor.class);
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionProcessor#processAction(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ExecutableAction)
	 */
	@Override
	public void processView(HttpServletRequest request, HttpServletResponse response, Executable action) throws IOException, ActionExecutionException {
		if (action instanceof DataView)
			processView(request, response, (DataView)action);
		else
			throw new IllegalArgumentException("Action not applicable here.");
	}
	
	public void processView(HttpServletRequest request, HttpServletResponse response, DataView action)
			throws IOException, ActionExecutionException {
		
		if (log.isDebugEnabled())
			log.debug("processView() - processing action "+action.getClass().getSimpleName());
	
		//write output
		action.writeData(response);
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
