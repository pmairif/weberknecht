/*
 * ProcessingException.java
 * 
 * Copyright 2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=utf-8, tabstob=4
 */
package com.github.pmairif.weberknecht.request.processing;

import com.github.pmairif.weberknecht.request.ExecutionException;

/**
 * problem while executing a {@link Processor}
 * 
 * @author pmairif
 */
@SuppressWarnings("serial")
public class ProcessingException extends ExecutionException {

	public ProcessingException() {
		//
	}

	public ProcessingException(String message) {
		super(message);
	}

	public ProcessingException(Throwable cause) {
		super(cause);
	}

	public ProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

}
