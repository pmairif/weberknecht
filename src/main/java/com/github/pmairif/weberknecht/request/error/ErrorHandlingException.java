/*
 * ErrorHandlingException.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 21.10.2012
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.error;

/**
 * exception that occurred while handling errors
 * 
 * @author pmairif
 */
@SuppressWarnings("serial")
public class ErrorHandlingException extends Exception {

	public ErrorHandlingException() {
		// 
	}

	public ErrorHandlingException(String msg) {
		super(msg);
	}

	public ErrorHandlingException(Throwable cause) {
		super(cause);
	}

	public ErrorHandlingException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
