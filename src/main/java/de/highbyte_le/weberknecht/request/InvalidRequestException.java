/*
 * InvalidRequestException.java (weberknecht)
 *
 * Copyright 2014 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;


/**
 * Invalid request or invalid parameter values from client.
 * Special Exception for easy handling within error handlers.
 *
 * @author pmairif
 */
public class InvalidRequestException extends ContentProcessingException {
    /**
     * use HTTP status '403 Forbidden'
     */
    public InvalidRequestException(String errDesc) {
        super(403, errDesc);
    }

    public InvalidRequestException(int httpStatusCode, String errDesc) {
        super(httpStatusCode, errDesc);
    }
}
