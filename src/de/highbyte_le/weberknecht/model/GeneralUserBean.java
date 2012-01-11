/*
 * GeneralUserBean.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 27.01.2009
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.model;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import de.highbyte_le.weberknecht.rpx.RpxAuthInfo;

/**
 * general user data
 * @author pmairif
 */
public class GeneralUserBean implements Serializable {
	private static final long serialVersionUID = -8546516417589436148L;
	
	/**
	 * local ID
	 */
	private int id = 0;
	
	/**
	 * global identifier
	 */
	private String identifier = null;
	
	private String displayName = null;
	
	private String email = null;
	
	private String verifiedEmail = null;
	
	private String url = null;
	
	public static GeneralUserBean getFromSession(HttpSession session) {
		GeneralUserBean user = (GeneralUserBean) session.getAttribute("user_bean"); //$NON-NLS-1$
		if (null == user)
			user = new GeneralUserBean();
		
		return user;
	}
	
	public void storeInSession(HttpSession session) {
		session.setAttribute("user_bean", this);
	}

	public static void clearSession(HttpSession session) {
		session.removeAttribute("user_bean");
	}

	public void populate(RpxAuthInfo authInfo) {
		setIdentifier( authInfo.getValue("identifier") );
		setDisplayName( authInfo.getValue("displayName") );
		setVerifiedEmail( authInfo.getValue("verifiedEmail") );
		setEmail( authInfo.getValue("email") );
		setUrl( authInfo.getValue("url") );
	}
	
	public GeneralUserBean() {
		// 
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	public String getIdentifier_htmlEscaped() {
		return StringEscapeUtils.escapeHtml(getIdentifier());
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public boolean hasDisplayName() {
		return (this.displayName != null && displayName.length() > 0);
	}
	
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	public String getDisplayName_htmlEscaped() {
		return StringEscapeUtils.escapeHtml(getDisplayName());
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public boolean hasEmail() {
		return (this.email != null && this.email.length() > 0);
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	public String getEmail_htmlEscaped() {
		return StringEscapeUtils.escapeHtml(getEmail());
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public boolean hasVerifiedEmail() {
		return (this.verifiedEmail != null && this.verifiedEmail.length() > 0);
	}

	public String getVerifiedEmail() {
		return this.verifiedEmail;
	}

	public String getVerifiedEmail_htmlEscaped() {
		return StringEscapeUtils.escapeHtml(getVerifiedEmail());
	}
	
	/**
	 * @param verifiedEmail the verifiedEmail to set
	 */
	public void setVerifiedEmail(String verifiedEmail) {
		this.verifiedEmail = verifiedEmail;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public boolean hasUrl() {
		return (this.url != null && this.url.length() > 0);
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getUrl_htmlEscaped() {
		return StringEscapeUtils.escapeHtml(this.url);
	}
}
