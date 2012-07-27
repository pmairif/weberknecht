/*
 * SigninPopupTag.java
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

/**
 * NOTE describe that class
 * @author pmairif
 */
public class SigninPopupTag extends RpxTaglibBase {

	private static final long serialVersionUID = -3270486176014733649L;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
			StringBuilder b = new StringBuilder();
			
			b.append("<a class=\"rpxnow\" onclick=\"return false;\""); 
			b.append(" href=\"https://rpxnow.com/openid/v2/signin?token_url=");
			b.append(URLEncoder.encode(getTokenUrl(), "UTF-8"));
			b.append("\">");
						
			pageContext.getOut().print(b.toString());
			return EVAL_BODY_INCLUDE;
		}
		catch (IOException e) {
			throw new JspException("i/o exception: "+e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().print("</a>");
			return EVAL_PAGE;
		}
		catch (IOException e) {
			throw new JspException("i/o exception: "+e.getMessage(), e);
		}
	}
}
