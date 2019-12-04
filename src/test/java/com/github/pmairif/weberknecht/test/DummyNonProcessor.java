/*
 * DummyNonProcessor.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pmairif.weberknecht.request.ContentProcessingException;
import com.github.pmairif.weberknecht.request.actions.ExecutableAction;
import com.github.pmairif.weberknecht.request.processing.ProcessingChain;
import com.github.pmairif.weberknecht.request.processing.ProcessingException;
import com.github.pmairif.weberknecht.request.routing.RoutingTarget;

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
