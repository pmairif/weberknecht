/*
 * DefaultErrorHandler.java (weberknecht)
 *
 * Copyright 2012-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pmairif.weberknecht.Controller;
import com.github.pmairif.weberknecht.request.routing.RoutingTarget;
import com.github.pmairif.weberknecht.request.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.pmairif.weberknecht.db.DBConnectionException;
import com.github.pmairif.weberknecht.request.ContentProcessingException;
import com.github.pmairif.weberknecht.request.actions.ActionExecutionException;
import com.github.pmairif.weberknecht.request.actions.ActionInstantiationException;
import com.github.pmairif.weberknecht.request.processing.ProcessingException;

/**
 * error handler used by default
 *
 * @author pmairif
 */
public class DefaultErrorHandler implements ErrorHandler {

	private int statusCode;

	/**
	 * Logger for this class
	 */
	private final Log log = LogFactory.getLog(Controller.class);

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ErrorHandler#handleException(java.lang.Exception, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean handleException(Exception exception, HttpServletRequest request, RoutingTarget routingTarget) {
		if (exception instanceof NotFoundException) {
			log.warn("resource not found: "+exception.getMessage()+"; request URI was "+request.getRequestURI());
			this.statusCode = HttpServletResponse.SC_NOT_FOUND;	//throw 404, if action doesn't exist
		}
		else if (exception instanceof ContentProcessingException) {
			log.error("doGet() - "+exception.getClass().getSimpleName()+": "+exception.getMessage());	//$NON-NLS-1$
			this.statusCode =  ((ContentProcessingException) exception).getHttpStatusCode();
			//TODO error page with error message or set request attribute to be able to write it on standard error pages 
		}
		else if (exception instanceof ActionInstantiationException) {
			log.warn("action could not be instantiated: "+exception.getMessage(), exception);
			this.statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;	//throw 500, if action could not instantiated
		}
		else if (exception instanceof ProcessingException) {
			log.error("doGet() - PreProcessingException: "+exception.getMessage(), exception);	//$NON-NLS-1$
			this.statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;	//throw 500
		}
		else if (exception instanceof DBConnectionException) {
			log.error("doGet() - DBConnectionException: "+exception.getMessage(), exception);	//$NON-NLS-1$
			this.statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;	//throw 500
		}
		else if (exception instanceof ActionExecutionException) {
			log.error("doGet() - ActionExecutionException: "+exception.getMessage(), exception);	//$NON-NLS-1$
			this.statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;	//throw 500
		}
		else {
			log.error("doGet() - "+exception.getClass().getSimpleName()+": "+exception.getMessage(), exception);	//$NON-NLS-1$
			this.statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;	//throw 500
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ErrorHandler#getStatus()
	 */
	@Override
	public int getStatus() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	protected void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/* (non-Javadoc)
	 * @see AutoView#getViewProcessorName()
	 */
	@Override
	public String getViewProcessorName() {
		return null;	//no view, let the controller use sendError()
	}
}
