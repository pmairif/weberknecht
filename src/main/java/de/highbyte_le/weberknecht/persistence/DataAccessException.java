/*
 * DataAccessException.java
 * 
 * Copyright 2009-2012 Patrick Mairif.
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
@SuppressWarnings("serial")
public class DataAccessException extends Exception {

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessException(String message) {
		super(message);
	}
}
