/*
 * ExecutionException.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=utf8
 */
package com.github.pmairif.weberknecht.request;

/**
 * problem while executing an action, a processor or sth. similar.
 */
@SuppressWarnings("serial")
public class ExecutionException extends Exception {

	public ExecutionException() {
		super();
	}

	public ExecutionException(String arg0) {
		super(arg0);
	}

	public ExecutionException(Throwable arg0) {
		super(arg0);
	}

	public ExecutionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
