/*
 * SiteBaseLinkTag.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 31.01.2009
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.taglibs;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.pmairif.weberknecht.conf.ContextConfig;

/**
 * NOTE describe that class
 * @author pmairif
 */
public class SiteBaseLinkTag extends TagSupport {
	private static final long serialVersionUID = 1888494941073965008L;
	
	/**
	 * Logger for this class
	 */
	private final Log logger = LogFactory.getLog(SiteBaseLinkTag.class);

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print(getBaseUrl());
			return SKIP_BODY;
		}
		catch (IOException e) {
			throw new JspException("i/o exception: "+e.getMessage(), e);
		}
	}
	
	protected String getBaseUrl() throws JspException {

        if (!(pageContext.getRequest() instanceof HttpServletRequest))
        	throw new JspException("unable to get a HttpServletRequest");

        StringBuilder baseUrl = new StringBuilder();
        
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        try {
        	ContextConfig conf = new ContextConfig();
        	baseUrl.append(conf.getValue("webapp_base_url"));
        	
            if (baseUrl.charAt(baseUrl.length()-1) != '/')
            	baseUrl.append("/");
		}
        catch (NamingException e) {
            logger.error("getTokenUrl() - naming exception while fetching webapp_base_url: "+e.getMessage()); //$NON-NLS-1$
            
            
            logger.warn("automatically generating base URL");
        	String contextPath = request.getContextPath();
        	String localHost = request.getLocalName();
        	int localPort = request.getLocalPort();
        	
        	if (baseUrl.length() > 0)	//just to be sure
        		baseUrl = new StringBuilder();
        	
        	baseUrl.append("http://").append(localHost).append(":").append(localPort+contextPath).append("/");
        }
        
        return baseUrl.toString();
	}
}
