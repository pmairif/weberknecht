/*
 * MockAction.java (weberknecht)
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

public class MockAction implements ExecutableAction {

	private static int callCount = 0;
	
	/* (non-Javadoc)
	 * @see ExecutableAction#execute(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void execute(HttpServletRequest request) throws IOException, ActionExecutionException,
            ContentProcessingException {
		incCallCount();
	}
	
	/**
	 * @param callCount the callCount to set
	 */
	public synchronized static void setCallCount(int callCount) {
		MockAction.callCount = callCount;
	}

	public synchronized static void incCallCount() {
		MockAction.callCount++;
	}
	
	/**
	 * @return the callCount
	 */
	public static int getCallCount() {
		return callCount;
	}
}
