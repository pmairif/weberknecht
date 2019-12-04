/*
 * ActionInstantiationException.java (weberknecht)
 *
 * Copyright 2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 20.01.2010
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.actions;

/**
 * problem while instantiation an action
 * @author pmairif
 */
public class ActionInstantiationException extends Exception {

	private static final long serialVersionUID = -2931705650317952128L;

	public ActionInstantiationException() {
		//
	}

	public ActionInstantiationException(String message) {
		super(message);
	}

	public ActionInstantiationException(Throwable cause) {
		super(cause);
	}

	public ActionInstantiationException(String message, Throwable cause) {
		super(message, cause);
	}
}
