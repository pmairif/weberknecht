/*
 * DataAccessException.java
 * 
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=utf-8, tabstob=4
 */
package de.highbyte_le.weberknecht.persistence;

/**
 * problem accessing data
 *
 * @author pmairif
 */
public class DataAccessException extends Exception {

	private static final long serialVersionUID = 7670761492680915767L;

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessException(String message) {
		super(message);
	}
}
