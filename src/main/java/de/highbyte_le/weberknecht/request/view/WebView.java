/*
 * WebView.java
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.view;

import java.util.Map;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.View;

/**
 * webapp view
 * 
 * @author pmairif
 */
public interface WebView {
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
	public Map<String, Object> getModels() throws ContentProcessingException;
}
