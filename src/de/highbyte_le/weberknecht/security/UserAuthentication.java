/*
 * UserAuthentication.java
 *
 * Copyright 2008 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: Jan 24, 2008
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.security;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

/**
 * hold general information about user authentication to be stored in the HTTP session
 * @author pmairif
 */
public class UserAuthentication implements Serializable {
	private static final long serialVersionUID = 9177669359989739L;

	private boolean authenticated = false;

	/**
	 * local user ID
	 */
	private Integer userId = null;
	
	public static UserAuthentication getAuth(HttpSession session) {
		UserAuthentication auth = (UserAuthentication) session.getAttribute("user_auth"); //$NON-NLS-1$
		if (null == auth)
			auth = new UserAuthentication(null, false);
		
		return auth;
	}
	
	public UserAuthentication(Integer userId, boolean authenticated) {
		this.userId = userId;
		this.authenticated = authenticated;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public boolean isAuthenticated() {
		return (userId != null && this.authenticated);
	}

	public void setAuthenticated(boolean authenticated, int userId) {
		this.authenticated = authenticated;
		
		if (authenticated)
			this.userId = new Integer(userId);
		else
			this.userId = null;
	}
}
