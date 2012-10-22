/*
 * JsonActionProcessor.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.view;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONWriter;

import de.highbyte_le.weberknecht.request.Executable;
import de.highbyte_le.weberknecht.request.actions.ActionExecutionException;

/**
 * process JSON views
 * 
 * @author pmairif
 */
public class JsonActionProcessor implements ActionViewProcessor {

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionProcessor#processView(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ExecutableAction)
	 */
	@Override
	public void processView(HttpServletRequest request, HttpServletResponse response, Executable action) throws IOException,
			ActionExecutionException {
		
		try {
			if (action instanceof JsonView)
				processView(request, response, (JsonView)action);
			else
				throw new IllegalArgumentException("Action not applicable here.");
		}
		catch (JSONException e) {
			throw new ActionExecutionException("JSON exception: "+e.getMessage(), e);
		}
	}

	public void processView(HttpServletRequest request, HttpServletResponse response, JsonView action)
			throws IOException, JSONException {
		
		response.setContentType( "application/json" );
		response.setCharacterEncoding("UTF-8");
		
		BufferedWriter writer = new BufferedWriter(response.getWriter());
		JSONWriter jsonWriter = new JSONWriter(writer);
		action.writeJson(jsonWriter);
		writer.flush();
		
		//TODO close writer?
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
