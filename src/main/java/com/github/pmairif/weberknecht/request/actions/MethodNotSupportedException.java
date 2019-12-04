/*
 * MethodNotSupportedException.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 11.08.2012
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.actions;

import com.github.pmairif.weberknecht.request.ContentProcessingException;

/**
 * HTTP Method is not supported.
 * Causes HTTP '405 Method Not Allowed'
 *
 * @author pmairif
 */
@SuppressWarnings("serial")
public class MethodNotSupportedException extends ContentProcessingException {

	private String method;
	
	public MethodNotSupportedException(String method) {
		super(405, "cannot handle HTTP "+method+"");
		this.method = method;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
}
