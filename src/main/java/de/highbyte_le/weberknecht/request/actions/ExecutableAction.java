/*
 * ExecutableAction.java
 *
 * Copyright 2009-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.Executable;
import de.highbyte_le.weberknecht.request.NotFoundException;

/**
 * executable webapp action
 * 
 * @author pmairif
 */
public interface ExecutableAction extends Executable {
	/**
	 * execute business logic
	 * 
	 * @throws ActionExecutionException
	 * 		if problems during execution occur
	 */
	void execute(HttpServletRequest request)
            throws IOException, ActionExecutionException, ContentProcessingException, NotFoundException;
}
