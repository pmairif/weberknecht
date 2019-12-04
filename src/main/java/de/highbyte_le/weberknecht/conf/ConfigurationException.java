/*
 * ConfigurationException.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.conf;

/**
 * Problem with weberknecht configuration occurred.
 *
 * @author pmairif
 */
@SuppressWarnings("serial")
public class ConfigurationException extends Exception {

	public ConfigurationException(String msg, Throwable e) {
		super(msg, e);
	}

	public ConfigurationException(Throwable e) {
		super(e);
	}

	public ConfigurationException() {
		//
	}

	public ConfigurationException(String msg) {
		super(msg);
	}
}
