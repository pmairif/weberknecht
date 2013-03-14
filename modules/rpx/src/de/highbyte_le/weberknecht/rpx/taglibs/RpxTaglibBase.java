/*
 * RpxTaglibBase.java
 *
 * Copyright 2009-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.rpx.taglibs;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.conf.ContextConfig;

/**
 * base class for RPX with common functionality for all tags
 * 
 * @author pmairif
 */
@SuppressWarnings("serial")
public abstract class RpxTaglibBase extends TagSupport {
	
	private String responsePath = null;
	
	/**
	 * Logger for this class
	 */
	private final Log logger = LogFactory.getLog(RpxTaglibBase.class);

	/**
	 * @return the responsePath
	 */
	public String getResponsePath() {
		return this.responsePath;
	}

	/**
	 * @param responsePath the responsePath to set
	 */
	public void setResponsePath(String responsePath) {
		this.responsePath = responsePath;
	}

	protected String getTokenUrl() throws JspException {

        //base url
		String webappBaseUrl = null;
        try {
        	ContextConfig conf = new ContextConfig();
            webappBaseUrl = conf.getValue("webapp_base_url");
		}
        catch (NamingException e) {
        	if (logger.isWarnEnabled())
        		logger.warn("getTokenUrl() - naming exception while fetching webapp_base_url: "+e.getMessage()); //$NON-NLS-1$
        	
            if (!(pageContext.getRequest() instanceof HttpServletRequest))
            	throw new JspException("unable to get a HttpServletRequest");

            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        	String contextPath = request.getContextPath();
        	String localHost = request.getLocalName();
        	int localPort = request.getLocalPort();
        	
        	StringBuilder b = new StringBuilder();
        	b.append("http://").append(localHost).append(":").append(localPort+contextPath).append("/");
        	webappBaseUrl = b.toString();

        	if (logger.isInfoEnabled())
        		logger.info("getTokenUrl() - automatically generating base URL '"+webappBaseUrl+"'");
        }
        
        return getTokenUrl(webappBaseUrl);
	}
	
	protected String getTokenUrl(String webappBaseUrl) throws JspException {
		StringBuilder tokenUrl = new StringBuilder();
		
		//base url
		tokenUrl.append(webappBaseUrl);
		
		if (tokenUrl.charAt(tokenUrl.length()-1) != '/')
			tokenUrl.append("/");
		
		//response path
		if (responsePath.length() > 0) {
			if (responsePath.charAt(0) == '/')
				tokenUrl.append(responsePath.substring(1));
			else
				tokenUrl.append(responsePath);
		}
		
		//parameter
		if (tokenUrl.toString().contains("?"))
			tokenUrl.append("&");
		else
			tokenUrl.append("?");
		tokenUrl.append("do=rpx");
		
		return tokenUrl.toString();
	}
}
