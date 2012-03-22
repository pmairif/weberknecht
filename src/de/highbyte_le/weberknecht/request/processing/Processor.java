/*
 * Processor.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.processing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.DatabaseCapable;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * @author pmairif
 */
public interface Processor extends DatabaseCapable {
	/**
	 * execute logic.
	 * 
	 * <p>The processor has to call {@link ProcessingChain#doContinue()} to continue the work of the chain.
	 * After all following processors in the chain are done, the doContinue method will return.</p>  
	 * 
	 * @throws ProcessingException
	 * 		if problems occurs during execution
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response, RoutingTarget routingTarget,
			ExecutableAction action, ProcessingChain chain)
	throws ProcessingException, ContentProcessingException;
}
