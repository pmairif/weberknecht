/*
 * DummyAction2.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.actions.ActionExecutionException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;

public class DummyAction2 implements ExecutableAction {

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.actions.ExecutableAction#execute(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void execute(HttpServletRequest request) throws IOException, ActionExecutionException,
			ContentProcessingException {
		//
	}
}
