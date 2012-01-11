/*
 * RpxTaglibBase.java
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 31.01.2009
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
public abstract class RpxTaglibBase extends TagSupport {
	
	private String responsePath = null;
	
	/**
	 * Logger for this class
	 */
	private final Log logger = LogFactory.getLog(RpxTaglibBase.class);

	private static final long serialVersionUID = 5366599250142507542L;

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

        if (!(pageContext.getRequest() instanceof HttpServletRequest))
        	throw new JspException("unable to get a HttpServletRequest");

        StringBuilder tokenUrl = new StringBuilder();
        
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        try {
        	ContextConfig conf = new ContextConfig();
            tokenUrl.append(conf.getValue("webapp_base_url"));
            
            if (tokenUrl.charAt(tokenUrl.length()-1) != '/')
            	tokenUrl.append("/");
		}
        catch (NamingException e) {
            logger.warn("getTokenUrl() - naming exception while fetching webapp_base_url: "+e.getMessage()); //$NON-NLS-1$
            
        	String contextPath = request.getContextPath();
        	String localHost = request.getLocalName();
        	int localPort = request.getLocalPort();
        	
        	if (tokenUrl.length() > 0)	//just to be sure
        		tokenUrl = new StringBuilder();
        	
        	tokenUrl.append("http://").append(localHost).append(":").append(localPort+contextPath).append("/");

            logger.info("getTokenUrl() - automatically generating base URL '"+tokenUrl.toString()+"'");
        }

    	tokenUrl.append(responsePath);
        
        if (tokenUrl.toString().contains("?"))
        	tokenUrl.append("&");
        else
        	tokenUrl.append("?");
        tokenUrl.append("do=rpx");
        
        return tokenUrl.toString();
	}
}
