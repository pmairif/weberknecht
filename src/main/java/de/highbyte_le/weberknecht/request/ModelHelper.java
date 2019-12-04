/*
 * ModelHelper.java (weberknecht)
 *
 * Copyright 2009-2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.request.actions.ExecutableAction;

/**
 * helps getting model instances from request attributes
 * 
 * @author pmairif
 */
public class ModelHelper {
	private final HttpServletRequest request;
	
	private final String modelPrefix;
	
	/**
	 * Logger for this class
	 */
	private final Log log = LogFactory.getLog(ModelHelper.class);
	
	public static final String DEFAULT_MODEL_PREFIX = "de.highbyte_le.weberknecht.model";
	
	public static final String ACTION_KEY = "de.highbyte_le.weberknecht.action";
	
	public ModelHelper(HttpServletRequest request, ServletContext context) {
		this.request = request;
		
		String mp = context.getInitParameter("model_prefix");
		this.modelPrefix = (mp != null ? mp : DEFAULT_MODEL_PREFIX);
		
		if (log.isDebugEnabled())
			log.debug("using model prefix "+modelPrefix);
	}

	public Object get(String name) {
		return request.getAttribute(getFullKey(name));
	}
	
	public void set(String name, Object obj) {
		request.setAttribute(getFullKey(name), obj);
	}
	
	public ExecutableAction getAction() {
		return (ExecutableAction) request.getAttribute(ACTION_KEY);
	}
	
	String getFullKey(String name) {
		StringBuilder b = new StringBuilder(modelPrefix).append('.').append(name);
		String fullKey = b.toString();
		
		if (log.isDebugEnabled())
			log.debug("key="+fullKey);
		
		return fullKey;
	}
}
