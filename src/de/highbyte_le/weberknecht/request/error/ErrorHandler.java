/*
 * ErrorHandler.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.error;

import javax.servlet.http.HttpServletRequest;

import de.highbyte_le.weberknecht.request.Executable;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;
import de.highbyte_le.weberknecht.request.view.AutoView;

/**
 * handles errors
 *
 * @author pmairif
 */
public interface ErrorHandler extends Executable, AutoView {
	/**
	 * handles exception
	 * 
	 * @param exception
	 * 		The exception to process.
	 */
	public void handleException(Exception exception, HttpServletRequest request, RoutingTarget routingTarget) throws ErrorHandlingException;
	
	/**
	 * get the HTTP Status code to return
	 */
	public int getStatus();
}
