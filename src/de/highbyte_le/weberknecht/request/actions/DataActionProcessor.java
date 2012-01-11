/*
 * DataActionProcessor.java (weberknecht)
 *
 * Copyright 2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 21.11.2010
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Process data actions
 * @author pmairif
 */
public class DataActionProcessor implements ActionViewProcessor {
	
	private final Log log = LogFactory.getLog(DataActionProcessor.class);
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionProcessor#processAction(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ExecutableAction)
	 */
	@Override
	public void processView(HttpServletRequest request, HttpServletResponse response, ExecutableAction action) throws IOException {
		if (action instanceof DataAction2)
			processView(request, response, (DataAction2)action);
		else
			throw new IllegalArgumentException("Action not applicable here.");
	}
	
	public void processView(@SuppressWarnings("unused") HttpServletRequest request, HttpServletResponse response, DataAction2 action)
			throws IOException {
		
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
}
