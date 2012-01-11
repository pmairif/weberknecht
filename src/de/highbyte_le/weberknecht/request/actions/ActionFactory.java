/*
 * ActionFactory.java
 *
 * Copyright 2008 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: Jan 22, 2008
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;


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
	
	public void registerAction(String name, String className);
}
