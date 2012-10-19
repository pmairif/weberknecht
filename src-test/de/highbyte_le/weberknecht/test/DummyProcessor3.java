/*
 * DummyProcessor2.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 16.10.2012
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.processing.ProcessingChain;
import de.highbyte_le.weberknecht.request.processing.ProcessingException;
import de.highbyte_le.weberknecht.request.processing.Processor;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * dummy processor
 *
 * @author pmairif
 */
public class DummyProcessor3 implements Processor {

	public DummyProcessor3() {
		//
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.processing.Processor#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.routing.RoutingTarget, de.highbyte_le.weberknecht.request.actions.ExecutableAction, de.highbyte_le.weberknecht.request.processing.ProcessingChain)
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, RoutingTarget routingTarget,
			ExecutableAction action, ProcessingChain chain) throws ProcessingException, ContentProcessingException {
		//
	}

}
