/*
 * DummyAction2.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.github.pmairif.weberknecht.request.ContentProcessingException;
import com.github.pmairif.weberknecht.request.actions.ActionExecutionException;
import com.github.pmairif.weberknecht.request.actions.ExecutableAction;

public class DummyAction2 implements ExecutableAction {

	/* (non-Javadoc)
	 * @see ExecutableAction#execute(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void execute(HttpServletRequest request) throws IOException, ActionExecutionException,
            ContentProcessingException {
		//
	}
}
