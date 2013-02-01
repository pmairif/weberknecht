/*
 * ActionBase.java (weberknecht)
 *
 * Copyright 2012-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.Localizable;

/**
 * optional base class for actions.
 *
 * @author pmairif
 */
public abstract class ActionBase implements TaskedExecutableAction, Localizable {
	
	/**
	 * Locale extracted from requested URL.
	 */
	private Locale requestedLocale = null;
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.actions.ExecutableAction#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void execute(HttpServletRequest request) throws IOException,
			ActionExecutionException, ContentProcessingException {
		execute(request, null);
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.actions.TaskedExecutableAction#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	@Override
	public void execute(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException {

		init(request);
		
		String method = request.getMethod();
		if ("get".equalsIgnoreCase(method))
			onGet(request, task);
		else if ("post".equalsIgnoreCase(method))
			onPost(request, task);
		else if ("put".equalsIgnoreCase(method))
			onPut(request, task);
		else if ("delete".equalsIgnoreCase(method))
			onDelete(request, task);
		else if ("head".equalsIgnoreCase(method))
			onHead(request, task);
		else if ("trace".equalsIgnoreCase(method))
			onTrace(request, task);
		else if ("options".equalsIgnoreCase(method))
			onOptions(request, task);
		
	}
	
	/**
	 * called before the onXXX methods
	 */
	protected void init(HttpServletRequest request) throws ActionExecutionException {
		//
	}
	
	/**
	 * called on HTTP GET requests.
	 * Override it to implement logic.
	 */
	protected void onGet(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException {
		throw new MethodNotSupportedException("GET");
	}

	/**
	 * called on HTTP POST requests.
	 * Override it to implement logic.
	 */
	protected void onPost(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException {
		throw new MethodNotSupportedException("POST");
	}
	
	/**
	 * called on HTTP PUT requests.
	 * Override it to implement logic.
	 */
	protected void onPut(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException {
		throw new MethodNotSupportedException("PUT");
	}
	
	/**
	 * called on HTTP DELETE requests.
	 * Override it to implement logic.
	 */
	protected void onDelete(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException {
		throw new MethodNotSupportedException("DELETE");
	}
	
	/**
	 * called on HTTP HEAD requests.
	 * Override it to implement logic.
	 */
	protected void onHead(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException {
		throw new MethodNotSupportedException("HEAD");
	}
	
	/**
	 * called on HTTP TRACE requests.
	 * Override it to implement logic.
	 */
	protected void onTrace(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException {
		throw new MethodNotSupportedException("TRACE");
	}
	
	/**
	 * called on HTTP OPTIONS requests.
	 * Override it to implement logic.
	 */
	protected void onOptions(HttpServletRequest request, String task) throws IOException,
			ActionExecutionException, ContentProcessingException {
		throw new MethodNotSupportedException("OPTIONS");
	}
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.Localizable#setRequestedLocale(java.util.Locale)
	 */
	@Override
	public void setRequestedLocale(Locale requestedLocale) {
		this.requestedLocale = requestedLocale;
	}
	
	/**
	 * get the locale extracted from the requested URL
	 * @return the requestedLocale
	 */
	protected Locale getRequestedLocale() {
		return requestedLocale;
	}
}
