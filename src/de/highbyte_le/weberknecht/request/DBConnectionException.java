/*
 * DBConnectionException.java
 * 
 * Copyright 2007 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

/**
 * error while connecting to database
 *
 * @author pmairif
 */
public class DBConnectionException extends Exception {

	private static final long serialVersionUID = -21554626899652052L;

	public DBConnectionException() {
        super();
    }

    public DBConnectionException(String message) {
        super(message);
    }

    public DBConnectionException(Throwable cause) {
        super(cause);
    }

    public DBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
