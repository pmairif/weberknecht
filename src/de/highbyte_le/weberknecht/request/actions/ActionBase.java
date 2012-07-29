/*
 * ActionBase.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 26.07.2012
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import de.highbyte_le.weberknecht.request.ContentProcessingException;

/**
 * optional base class for actions.
 *
 * @author rick
 */
public abstract class ActionBase implements TaskedAction {
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.actions.ExecutableAction#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void execute(HttpServletRequest request) throws IOException,
			ActionExecutionException, ContentProcessingException {
		execute(request, null);
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.actions.TaskedExecutableAction#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	@Override
	public void execute(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException {

		String method = request.getMethod();
		if ("get".equalsIgnoreCase(method))
			onGet(request, task);
		else if ("post".equalsIgnoreCase(method))
			onPost(request, task);
		
		//TODO other methods, too
	}
	
	public abstract void onGet(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException;

	public abstract void onPost(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException;
}
