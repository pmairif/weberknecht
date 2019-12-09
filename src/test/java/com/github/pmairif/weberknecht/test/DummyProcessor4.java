/*
 * DummyProcessor2.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 16.10.2012
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pmairif.weberknecht.request.ContentProcessingException;
import com.github.pmairif.weberknecht.request.actions.ExecutableAction;
import com.github.pmairif.weberknecht.request.processing.ProcessingChain;
import com.github.pmairif.weberknecht.request.processing.ProcessingException;
import com.github.pmairif.weberknecht.request.processing.Processor;
import com.github.pmairif.weberknecht.request.routing.RoutingTarget;

/**
 * dummy processor
 *
 * @author pmairif
 */
public class DummyProcessor4 implements Processor {

	public DummyProcessor4() {
		//
	}

	/* (non-Javadoc)
	 * @see Processor#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, RoutingTarget, ExecutableAction, ProcessingChain)
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, RoutingTarget routingTarget,
                        ExecutableAction action, ProcessingChain chain) throws ProcessingException, ContentProcessingException {
		//
	}

}
