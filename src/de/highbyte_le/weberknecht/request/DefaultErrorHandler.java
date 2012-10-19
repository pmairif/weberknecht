/*
 * DefaultErrorHandler.java (weberknecht)
 *
 * Copyright 2012 <Your Name>.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 19.10.2012
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.Controller;
import de.highbyte_le.weberknecht.db.DBConnectionException;
import de.highbyte_le.weberknecht.request.actions.ActionExecutionException;
import de.highbyte_le.weberknecht.request.actions.ActionInstantiationException;
import de.highbyte_le.weberknecht.request.actions.ActionNotFoundException;
import de.highbyte_le.weberknecht.request.processing.ProcessingException;

/**
 * NOTE describe that class
 *
 * @author rick
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
	public void handleException(Exception exception, HttpServletRequest request) {
		if (exception instanceof ActionNotFoundException) {
			log.warn("action not found: "+exception.getMessage()+"; request URI was "+request.getRequestURI());
			this.statusCode = HttpServletResponse.SC_NOT_FOUND;	//throw 404, if action doesn't exist
		}
		else if (exception instanceof ContentProcessingException) {
			log.error("doGet() - ContentProcessingException: "+exception.getMessage());	//$NON-NLS-1$
			this.statusCode =  ((ContentProcessingException) exception).getHttpStatusCode();
			//TODO write the error message to output 
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

	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ErrorHandler#getStatus()
	 */
	@Override
	public int getStatus() {
		return statusCode;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ErrorHandler#getView()
	 */
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ErrorHandler#getModels()
	 */
	@Override
	public Map<String, Object> getModels() throws ContentProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

}
