/*
 * LoadFunctionsTag.java
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 31.01.2009
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.rpx.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * NOTE describe that class
 * @author pmairif
 */
public class LoadFunctionsTag extends RpxTaglibBase {

	private String lang = "en";
	
	private String realm = null;
	
	private static final long serialVersionUID = -5321414279454137444L;

	/**
	 * @return the lang
	 */
	public String getLang() {
		return this.lang;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the realm
	 */
	public String getRealm() {
		return this.realm;
	}

	/**
	 * @param realm the realm to set
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		
		try {
			StringBuilder b = new StringBuilder();
			
			b.append("<script src=\"https://rpxnow.com/openid/v2/widget\" type=\"text/javascript\"></script>");
			b.append("<script type=\"text/javascript\">");
			b.append("RPXNOW.token_url = \"").append(getTokenUrl()).append("\";");
			b.append("RPXNOW.realm = \"").append(realm).append("\";");
			b.append("RPXNOW.overlay = true;");
			b.append("RPXNOW.language_preference = '").append(lang).append("';");
			b.append("</script>");
			
			out.print(b.toString());
		}
		catch (IOException e) {
			throw new JspException("i/o exception: "+e.getMessage(), e);
		}
		
		return SKIP_BODY;
	}

}
