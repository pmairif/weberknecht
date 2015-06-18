/*
 * ProcessingChain.java (weberknecht)
 *
 * Copyright 2010-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.processing;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.ExecutionException;
import de.highbyte_le.weberknecht.request.NotFoundException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * Processing chain.
 * 
 * @author pmairif
 */
public class ProcessingChain {
	private final HttpServletRequest request;
	
	private final HttpServletResponse response;
	
	private final ExecutableAction action;
	
	private final RoutingTarget routingTarget;
	
	private Iterator<Processor> it;
	
	public ProcessingChain(List<Processor> chain, HttpServletRequest request, HttpServletResponse response,
			RoutingTarget routingTarget, ExecutableAction action) {
		this.it = chain.iterator();

		this.request = request;
		this.response = response;
		this.action = action;
		this.routingTarget = routingTarget;
	}

	/**
	 * Continue processing. Returns after all processors are done. Which allows cleanup operations. 
	 */
	public void doContinue() throws ExecutionException, ContentProcessingException, RedirectException, NotFoundException {
		if (it.hasNext()) {
			it.next().execute(request, response, routingTarget, action, this);
		}
	}
}
