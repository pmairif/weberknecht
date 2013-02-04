/*
 * RedirectException.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.processing;

/**
 * The exception is thrown, if a processor wants to stop the chain processing an initiate a redirect.
 *
 * @author pmairif
 */
@SuppressWarnings("serial")
public class RedirectException extends Exception {
	private final String localRedirectDestination;

	/**
	 * pass the redirect destination, which can be local
	 * @param localRedirectDestination
	 */
	public RedirectException(String localRedirectDestination) {
		super();
		this.localRedirectDestination = localRedirectDestination;
	}
	
	/**
	 * @return the localRedirectDestination
	 */
	public String getLocalRedirectDestination() {
		return localRedirectDestination;
	}
}
