/*
 * Processor.java (weberknecht)
 *
 * Copyright 2010-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.processing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pmairif.weberknecht.request.routing.RoutingTarget;
import com.github.pmairif.weberknecht.request.ContentProcessingException;
import com.github.pmairif.weberknecht.request.ExecutionException;
import com.github.pmairif.weberknecht.request.NotFoundException;
import com.github.pmairif.weberknecht.request.actions.ExecutableAction;

/**
 * @author pmairif
 */
public interface Processor {
	/**
	 * execute logic.
	 * 
	 * <p>The processor has to call {@link ProcessingChain#doContinue()} to continue the work of the chain.
	 * After all following processors in the chain are done, the doContinue method will return.</p>  
	 * 
	 * @throws ExecutionException
	 * 		if problems occurs during execution
	 * @throws ContentProcessingException
	 * @throws RedirectException
	 */
	void execute(HttpServletRequest request, HttpServletResponse response, RoutingTarget routingTarget,
			ExecutableAction action, ProcessingChain chain)
            throws ExecutionException, ContentProcessingException, RedirectException, NotFoundException;
}
