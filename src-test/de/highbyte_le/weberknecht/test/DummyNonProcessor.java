/*
 * DummyNonProcessor.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.processing.ProcessingChain;
import de.highbyte_le.weberknecht.request.processing.ProcessingException;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * dummy processor, not implementing the Processor interface.
 *
 * @author pmairif
 */
public class DummyNonProcessor {

	public DummyNonProcessor() {
		//
	}

	public void execute(HttpServletRequest request, HttpServletResponse response, RoutingTarget routingTarget,
			ExecutableAction action, ProcessingChain chain) throws ProcessingException, ContentProcessingException {
		//
	}

}
