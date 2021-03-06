/*
 * UserLoggedInTestTag.java
 *
 * Copyright 2008-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: Jan 31, 2008
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.security.taglibs;

import javax.servlet.jsp.tagext.TagSupport;

import com.github.pmairif.weberknecht.security.UserAuthentication;

/**
 * @author pmairif
 */
@SuppressWarnings("serial")
public class UserLoggedInTestTag extends TagSupport {

	public UserLoggedInTestTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() {
		UserAuthentication auth = UserAuthentication.getAuth( pageContext.getSession() );
		if (auth.isAuthenticated()) {
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}
}
