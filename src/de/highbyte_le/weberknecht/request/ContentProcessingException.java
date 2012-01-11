/*
 * ContentProcessingException.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 19.01.2010
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

/**
 * exception while processing content
 * @author pmairif
 */
public class ContentProcessingException extends Exception {

	private final int httpStatusCode;
	
	private final String errDesc;
	
	private static final long serialVersionUID = 608004509191979682L;

	/**
	 * @param httpStatusCode
	 * 		HTTP status code to be sent back to the client
	 * @param errDesc
	 * 		Short error description sent back to the client
	 */
	public ContentProcessingException(int httpStatusCode, String errDesc) {
		this.httpStatusCode = httpStatusCode;
		this.errDesc = errDesc;
	}

	
	/**
	 * @return the errDesc
	 */
	public String getErrDesc() {
		return this.errDesc;
	}
	
	/**
	 * @return the httpStatusCode
	 */
	public int getHttpStatusCode() {
		return this.httpStatusCode;
	}
}
