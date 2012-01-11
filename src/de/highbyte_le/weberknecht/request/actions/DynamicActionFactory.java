/*
 * DynamicActionFactory.java
 *
 * Copyright 2008-2011 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: Jan 22, 2008
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * create actions by name
 * @author pmairif
 */
@SuppressWarnings("nls")
public class DynamicActionFactory implements ActionFactory {
	
	private Map<String, Class<? extends ExecutableAction>> actionMap = new HashMap<String, Class<? extends ExecutableAction>>();
	
	/**
	 * Logger for this class
	 */
	private final Log log = LogFactory.getLog(DynamicActionFactory.class);

	/**
	 * creates new instance for requested action 
	 * @param actionName
	 * @return new instance of action
	 */
	@Override
	public ExecutableAction createAction(String actionName) throws ActionInstantiationException {
		try {
			Class<? extends ExecutableAction> actionClass = actionMap.get(actionName);
			if (actionClass != null)
				return actionClass.newInstance();
		}
		catch (IllegalAccessException e) {
			log.error("createAction() - IllegalAccessException: "+e.getMessage(), e);
		}
		catch (InstantiationException e) {
			log.error("createAction() - InstantiationException: "+e.getMessage(), e);
		}
		catch (Exception e) {
			throw new ActionInstantiationException(e.getMessage(), e);
		}
		
		return null;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void registerAction(String name, String className) {
		try {
			
			Class<?> c = Class.forName(className);
			Object o = c.newInstance();
			
			if (o instanceof ExecutableAction) {
				actionMap.put(name, (Class<ExecutableAction>)c);
			}
			else {
				log.error(className+" is not an executable action");
			}

		}
		catch (ClassNotFoundException e) {
			log.error("registerAction() - ClassNotFoundException: "+e.getMessage(), e);
		}
		catch (InstantiationException e) {
			log.error("registerAction() - InstantiationException: "+e.getMessage(), e);
		}
		catch (IllegalAccessException e) {
			log.error("registerAction() - IllegalAccessException: "+e.getMessage(), e);
		}
	}
}
