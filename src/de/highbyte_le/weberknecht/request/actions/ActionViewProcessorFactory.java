/*
 * ActionProcessorFactory.java (weberknecht)
 *
 * Copyright 2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 21.11.2010
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * create action processors
 * 
 * @author pmairif
 */
public class ActionViewProcessorFactory {
	//TODO use configuration file to register custom processors (eg Calendar)
	
	private static final Log log = LogFactory.getLog(ActionViewProcessor.class);
	
	private final Map<String, Class<? extends ActionViewProcessor>> suffixProcessorMap = new HashMap<String, Class<? extends ActionViewProcessor>>();
	
	public ActionViewProcessorFactory() {
		//register default processors
		registerProcessor("do", WebActionProcessor.class);
		registerProcessor("feed", FeedActionProcessor.class);
		registerProcessor("json", JsonActionProcessor.class);
		registerProcessor("data", DataActionProcessor.class);
	}
	
	/**
	 * @param viewProcessorName
	 * 		unique name identifying the view processor (formerly called suffix)
	 */
	public ActionViewProcessor createActionProcessor(String viewProcessorName) {
		ActionViewProcessor processor = null;
		
		Class<? extends ActionViewProcessor> clazz = suffixProcessorMap.get(viewProcessorName);
		if (clazz != null) {
			try {
				processor = clazz.newInstance();
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
