/*
 * NotFoundException.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

/**
 * a requested resource is not available
 */
public class NotFoundException extends Exception {
    /**
     * requested resource
     */
    private String resource;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, String resource) {
        super(message);
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
