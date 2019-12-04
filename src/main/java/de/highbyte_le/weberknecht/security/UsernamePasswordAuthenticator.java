/*
 * UsernamePasswordAuthenticator.java
 *
 * Copyright 2008 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: Jan 25, 2008
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.security;

/**
 * Authenticator by user name and password
 * @author pmairif
 */
public interface UsernamePasswordAuthenticator extends Authenticator {
	/**
	 * validate user name and password
	 * @param userName	the user name
	 * @param password	the password
	 * @return	user id or null, if authentication fails
	 */
	public Integer authenticate(String userName, String password);

}
