/*
 * ContextConfig.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 2009-02-01
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.conf;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * helper for accessing JNDI config
 * @author pmairif
 */
public class ContextConfig {
	
	private Context envCtx = null;
	
	/**
	 * Logger for this class
	 */
	private final Log logger = LogFactory.getLog(ContextConfig.class);

	public ContextConfig() throws NamingException {
		Context ctx = new InitialContext();
        envCtx = (Context) ctx.lookup("java:comp/env"); //$NON-NLS-1$
	}
	
	public String getValue(String key) throws NamingException {
        return (String) envCtx.lookup(key);
	}
	
	public String getValue(String key, String defaultValue) {
		try {
			return getValue(key);
		}
		catch (NamingException e) {
			logger.error("getValue() - NamingException: "+e.getMessage(), e);	//$NON-NLS-1$
		}
		return defaultValue;
	}
}
