/*
 * ErrorHandler.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 19.10.2012
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.error;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.View;

/**
 * handles errors
 *
 * @author pmairif
 */
public interface ErrorHandler {
	/**
	 * handles exception
	 * 
	 * @param exception
	 * 		The exception to process.
	 */
	public void handleException(Exception exception, HttpServletRequest request) throws ErrorHandlingException;
	
	/**
	 * get the HTTP Status code to return
	 */
	public int getStatus();
	
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
