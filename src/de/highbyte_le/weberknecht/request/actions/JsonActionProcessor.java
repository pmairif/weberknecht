/*
 * JsonActionProcessor.java (weberknecht)
 *
 * Copyright 2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 21.11.2010
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONWriter;

import de.highbyte_le.weberknecht.request.error.ErrorHandler;

/**
 * process json actions
 * 
 * @author pmairif
 */
public class JsonActionProcessor implements ActionViewProcessor {

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionProcessor#processView(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ExecutableAction)
	 */
	@Override
	public void processView(HttpServletRequest request, HttpServletResponse response, ExecutableAction action) throws IOException,
			ActionExecutionException {
		
		try {
			if (action instanceof JsonAction)
				processView(request, response, (JsonAction)action);
			else
				throw new IllegalArgumentException("Action not applicable here.");
		}
		catch (JSONException e) {
			throw new ActionExecutionException("JSON exception: "+e.getMessage(), e);
		}
	}

	public void processView(HttpServletRequest request, HttpServletResponse response, JsonAction action)
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

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.actions.ActionViewProcessor#processView(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ErrorHandler)
	 */
	@Override
	public void processView(HttpServletRequest request, HttpServletResponse response, ErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		
	}
}
