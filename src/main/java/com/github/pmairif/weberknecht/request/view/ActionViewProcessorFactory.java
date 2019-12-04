/*
 * ActionProcessorFactory.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.view;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * create action processors
 * 
 * Custom processors can be configured via action-view-processors/action-view-processor in weberknecht.xml.
 * 
 * @author pmairif
 */
public class ActionViewProcessorFactory {
	private static final Log log = LogFactory.getLog(ActionViewProcessor.class);
	
	private final Map<String, Class<? extends ActionViewProcessor>> suffixProcessorMap = new HashMap<String, Class<? extends ActionViewProcessor>>();
	
	public ActionViewProcessorFactory() {
		//register default processors
		registerProcessor("do", WebActionProcessor.class);
		registerProcessor("", WebActionProcessor.class);
		registerProcessor("feed", FeedActionProcessor.class);
		registerProcessor("json", JsonActionProcessor.class);
		registerProcessor("data", DataActionProcessor.class);
	}
	
	/**
	 * @param viewProcessorName
	 * 		unique name identifying the view processor (formerly called suffix)
	 */
	public ActionViewProcessor createActionProcessor(String viewProcessorName, ServletContext servletContext) {
		ActionViewProcessor processor = null;
		
		Class<? extends ActionViewProcessor> clazz = suffixProcessorMap.get(viewProcessorName);
		if (clazz != null) {
			try {
				processor = clazz.newInstance();
				processor.setServletContext(servletContext);
				processor.setActionViewProcessorFactory(this);
			}
			catch (InstantiationException e) {
				log.error("createActionProcessor() - InstantiationException: "+e.getMessage(), e);	//$NON-NLS-1$
			}
			catch (IllegalAccessException e) {
				log.error("createActionProcessor() - IllegalAccessException: "+e.getMessage(), e);	//$NON-NLS-1$
			}
		}
	
		return processor;
	}
	
	@SuppressWarnings("unchecked")
	public void registerProcessor(String suffix, String className) throws ClassNotFoundException {
		Class<ActionViewProcessor> clazz = (Class<ActionViewProcessor>) Class.forName(className);
		registerProcessor(suffix, clazz);
	}

	public void registerProcessor(String suffix, Class<? extends ActionViewProcessor> clazz) {
		suffixProcessorMap.put(suffix, clazz);
	}
}
