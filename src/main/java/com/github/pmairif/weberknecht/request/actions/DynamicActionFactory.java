/*
 * DynamicActionFactory.java
 *
 * Copyright 2008-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.actions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final Logger log = LoggerFactory.getLogger(DynamicActionFactory.class);

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
	
	/* (non-Javadoc)
	 * @see ActionFactory#hasAction(java.lang.String)
	 */
	@Override
	public boolean knowsAction(String actionName) {
		return actionMap.containsKey(actionName);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void registerAction(String name, String className) throws ActionInstantiationException {
		try {
			
			Class<?> c = Class.forName(className);
			Object o = c.newInstance();
			
			if (o instanceof ExecutableAction) {
				actionMap.put(name, (Class<ExecutableAction>)c);
			}
			else {
				throw new ActionInstantiationException(className+" is not a valid action.");
			}

		}
		catch (Exception e) {
			throw new ActionInstantiationException("problems instantiating "+className+": "+e.getMessage(), e);
		}
	}
}
