/*
 * RpxException.java
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.rpx;

/**
 * @author pmairif
 */
public class RpxException extends Exception {

	private static final long serialVersionUID = -7477693481306234014L;

	public RpxException() {
		//
	}

	public RpxException(String message) {
		super(message);
	}

	public RpxException(Throwable cause) {
		super(cause);
	}

	public RpxException(String message, Throwable cause) {
		super(message, cause);
	}
}
