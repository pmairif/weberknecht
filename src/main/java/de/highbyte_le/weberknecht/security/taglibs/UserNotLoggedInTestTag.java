/*
 * UserLoggedInTestTag.java
 *
 * Copyright 2008-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: Jan 31, 2008
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.security.taglibs;

import javax.servlet.jsp.tagext.TagSupport;

import de.highbyte_le.weberknecht.security.UserAuthentication;

/**
 * @author pmairif
 */
@SuppressWarnings("serial")
public class UserNotLoggedInTestTag extends TagSupport {

	public UserNotLoggedInTestTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() {
		UserAuthentication auth = UserAuthentication.getAuth( pageContext.getSession() );
		if (auth.isAuthenticated()) {
			return SKIP_BODY;
		}
		return EVAL_BODY_INCLUDE;
	}
}
