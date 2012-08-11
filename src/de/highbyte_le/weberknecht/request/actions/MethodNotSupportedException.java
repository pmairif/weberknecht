/*
 * MethodNotSupportedException.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 11.08.2012
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

/**
 * HTTP Method ist not supported.
 *
 * @author pmairif
 */
@SuppressWarnings("serial")
public class MethodNotSupportedException extends ActionExecutionException {

	private String method;
	
	public MethodNotSupportedException(String method) {
		super("cannot handle HTTP "+method+"");
		this.method = method;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
}
