/*
 * ModelHelper.java (weberknecht)
 *
 * Copyright 2009-2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.github.pmairif.weberknecht.request.actions.ExecutableAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final Logger log = LoggerFactory.getLogger(ModelHelper.class);
	
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
