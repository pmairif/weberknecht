/*
 * ContentProcessingException.java (weberknecht)
 *
 * Copyright 2009-2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request;

/**
 * exception while processing content
 * @author pmairif
 */
@SuppressWarnings("serial")
public class ContentProcessingException extends Exception {

	private final int httpStatusCode;
	
	/**
	 * @param httpStatusCode
	 * 		HTTP status code to be sent back to the client
	 * @param message
	 * 		Short error message potentially viewable by the client
	 */
	public ContentProcessingException(int httpStatusCode, String message) {
        super(message);
		this.httpStatusCode = httpStatusCode;
	}

	/**
	 * @return the errDesc
     * @deprecated use getMessage()
	 */
    @Deprecated
	public String getErrDesc() {
		return getMessage();
	}
	
	/**
	 * @return the httpStatusCode
	 */
	public int getHttpStatusCode() {
		return this.httpStatusCode;
	}
}
