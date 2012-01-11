/*
 * ExecutableAction.java
 *
 * Copyright 2009-2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.ContentProcessingException;

/**
 * executable webapp action
 * 
 * @author pmairif
 */
public interface ExecutableAction {
	/**
	 * execute business logic
	 * 
	 * @throws ActionExecutionException
	 * 		if problems during execution occur
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, ActionExecutionException, ContentProcessingException;
}
