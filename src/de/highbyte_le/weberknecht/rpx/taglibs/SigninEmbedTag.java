/*
 * SigninEmbedTag.java
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 31.01.2009
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.rpx.taglibs;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * NOTE describe that class
 * @author pmairif
 */
public class SigninEmbedTag extends RpxTaglibBase {

	private static final long serialVersionUID = 6234002328583362847L;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		
		try {
			StringBuilder b = new StringBuilder();
			
			b.append("<iframe class=\"rpxframe\" src=\"https://rpxnow.com/openid/embed?token_url=");
			b.append(URLEncoder.encode(getTokenUrl(), "UTF-8"));
			b.append("\" scrolling=\"no\" frameBorder=\"no\" style=\"width:400px;height:240px;\"></iframe>");
			
			out.print(b.toString());
		}
		catch (IOException e) {
			throw new JspException("i/o exception: "+e.getMessage(), e);
		}
		
		return SKIP_BODY;
	}
}
