/*
 * ExecutableAction.java
 *
 * Copyright 2009-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.github.pmairif.weberknecht.request.ContentProcessingException;
import com.github.pmairif.weberknecht.request.NotFoundException;

/**
 * executable webapp action supporting tasks
 * 
 * @author pmairif
 */
public interface TaskedExecutableAction extends ExecutableAction {
	/**
	 * execute business logic
	 * 
	 * @param task
	 * 		optional task - the string after the '!' in the URL.
	 * @throws ActionExecutionException
	 * 		if problems during execution occur
	 */
	void execute(HttpServletRequest request, String task)
            throws IOException, ActionExecutionException, ContentProcessingException, NotFoundException;
}
