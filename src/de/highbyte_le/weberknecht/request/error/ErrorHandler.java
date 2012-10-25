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
 * <p>If you want the error pages to be used that are configured in your web.xml,
 * just let {@link #getViewProcessorName()} return null and {@link #getStatus()} the error code you want.
 * </p>
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
	 * 
	 * <p>If the error handler returns any view, the status code is set with setStatus,
	 * else the code is send via sendError() to display the standard error page.</p>
	 */
	public int getStatus();
}
