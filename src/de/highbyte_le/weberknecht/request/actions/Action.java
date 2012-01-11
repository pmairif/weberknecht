/*
 * Action.java
 *
 * Copyright 2007 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 2007-09-01
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.util.Map;

import de.highbyte_le.weberknecht.request.DatabaseCapable;
import de.highbyte_le.weberknecht.request.View;

/**
 * webapp actions
 * 
 * @author pmairif
 */
public interface Action extends DatabaseCapable, ExecutableAction {
	/**
	 * return the view information
	 */
	public View getView();
	
	/**
	 * return a mapping of names to models.
	 * 
	 * <p>The names are appended to de.highbyte_le.weberknecht.model and used as attribute names.</p>
	 * 
	 * @return mapping of names to models
	 */
	public Map<String, Object> getModels();
}
