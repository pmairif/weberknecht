/*
 * ActionNotFoundException.java
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 08.02.2009
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import de.highbyte_le.weberknecht.request.NotFoundException;

/**
 * Action not found.
 * 
 * @author pmairif
 */
@SuppressWarnings("serial")
public class ActionNotFoundException extends NotFoundException {

	public ActionNotFoundException() {
		super("action not found");
	}

    public ActionNotFoundException(String message) {
        super(message);
    }

    public ActionNotFoundException(String message, String resource) {
        super(message, resource);
    }
}
