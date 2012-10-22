/*
 * WebActionProcessor.java (weberknecht)
 *
 * Copyright 2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 21.11.2010
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.ModelHelper;
import de.highbyte_le.weberknecht.request.View;
import de.highbyte_le.weberknecht.request.error.ErrorHandler;

/**
 * Process usual web actions
 * 
 * @author pmairif
 */
public class WebActionProcessor implements ActionViewProcessor {
	
	private final Log log = LogFactory.getLog(WebActionProcessor.class);
	
	private ServletContext servletContext = null;
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionViewProcessor#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionProcessor#processAction(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ExecutableAction)
	 */
	@Override
	public void processView(HttpServletRequest request, HttpServletResponse response, ExecutableAction action) throws ServletException,
			IOException, ContentProcessingException {
		
		if (action instanceof WebAction)
			processView(request, response, (WebAction)action);
		else
			throw new IllegalArgumentException("Action not applicable here.");
	}
	
	public void processView(HttpServletRequest request, HttpServletResponse response, WebAction action) throws ServletException, IOException, ContentProcessingException {
		if (log.isDebugEnabled())
			log.debug("processView() - processing action "+action.getClass().getSimpleName());

		View view = action.getView();
		if (view != null && view.hasJspFileName()) {
			processJsp(request, response, view, action.getModels(), action);	//(Don't fetch model on redirect!!)
		}
		else if (view != null && view.hasRedirection()) {
			processRedirect(response, view);
		}
		else {
			log.warn("processView() - " + action.getClass().getName()+" produced no view and no redirection");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.actions.ActionViewProcessor#processView(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ErrorHandler)
	 */
	@Override
	public void processView(HttpServletRequest request, HttpServletResponse response, ErrorHandler errorHandler) {
		if (log.isDebugEnabled())
			log.debug("processView() - processing error handler "+errorHandler.getClass().getSimpleName());

		try {
			View view = errorHandler.getView();
			if (view != null && view.hasJspFileName()) {
				processJsp(request, response, view, errorHandler.getModels(), errorHandler);	//(Don't fetch model on redirect!!)
			}
			else if (view != null && view.hasRedirection()) {
				processRedirect(response, view);
			}
			else {
				log.warn("processView() - " + errorHandler.getClass().getName()+" produced no view and no redirection");
			}
		}
		catch (Exception e) {
			log.error("processView() - ServletException: "+e.getMessage(), e);	//$NON-NLS-1$
		}
	}

	protected void processJsp(HttpServletRequest request, HttpServletResponse response, View view, Map<String, Object> modelMap, Object action)
			throws ServletException, IOException {
		if (log.isDebugEnabled())
			log.debug("processView() - forward the request to the given view: "+view.getJspFileName());
		
		//add models to request
		if (modelMap != null) {
			ModelHelper modelHelper = new ModelHelper(request, servletContext);
			for (Entry<String, Object> e: modelMap.entrySet()) {
				modelHelper.set(e.getKey(), e.getValue());
			}
		}
		
		//forward the request to the given view
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/"+view.getJspFileName());
		dispatcher.forward(request, response);
	}

	protected void processRedirect(HttpServletResponse response, View view) {
		if (log.isDebugEnabled())
			log.debug("processRedirect() - initiate redirection to: "+view.getRedirection());
		
		response.setHeader("Location", view.getRedirection().toExternalForm());
		response.setStatus(303);	//303 - "see other" (http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)
	}
}
