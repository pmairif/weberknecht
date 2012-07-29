/*
 * ActionExecutionException.java
 * 
 * Copyright 2007 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=utf-8, tabstob=4
 */
package de.highbyte_le.weberknecht.request.actions;

/**
 * problem while executing an {@link WebAction}
 * @author pmairif
 */
@SuppressWarnings("serial")
public class ActionExecutionException extends Exception {

	public ActionExecutionException() {
		//
	}

	public ActionExecutionException(String message) {
		super(message);
	}

	public ActionExecutionException(Throwable cause) {
		super(cause);
	}

	public ActionExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

}
