/*
 * ActionFactory.java
 *
 * Copyright 2008-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.actions;

/**
 * create actions by name
 * @author pmairif
 */
public interface ActionFactory {
	/**
	 * creates new instance for requested action 
	 * @param actionName
	 * @return new instance of action
	 */
	public ExecutableAction createAction(String actionName) throws ActionInstantiationException;
	
	/**
	 * is the requested action known?
	 * @param actionName
	 * @return true, if action is known
	 */
	public boolean knowsAction(String actionName);
	
	public void registerAction(String name, String className) throws ActionInstantiationException;
}
