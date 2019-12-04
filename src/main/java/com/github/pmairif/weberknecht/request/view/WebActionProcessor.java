/*
 * WebActionProcessor.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.view;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pmairif.weberknecht.request.ContentProcessingException;
import com.github.pmairif.weberknecht.request.Executable;
import com.github.pmairif.weberknecht.request.ModelHelper;
import com.github.pmairif.weberknecht.request.View;

/**
 * Process usual web actions
 * 
 * @author pmairif
 */
public class WebActionProcessor implements ActionViewProcessor {
	
	private final Logger log = LoggerFactory.getLogger(WebActionProcessor.class);
	
	private ServletContext servletContext = null;
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionProcessor#processAction(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ExecutableAction)
	 */
	@Override
	public boolean processView(HttpServletRequest request, HttpServletResponse response, Executable action) throws ServletException,
			IOException, ContentProcessingException {
		
		if (action instanceof WebView) {
			return processView(request, response, (WebView) action);
		}
		
		throw new IllegalArgumentException("Action not applicable here.");
	}
	
	public boolean processView(HttpServletRequest request, HttpServletResponse response, WebView action) throws ServletException, IOException, ContentProcessingException {
		if (log.isDebugEnabled()) {
			log.debug("processView() - processing action " + action.getClass().getSimpleName());
		}

		boolean processed = false;
		View view = action.getView();
		if (view != null && view.hasJspFileName()) {
			processJsp(request, response, view, action.getModels(), action);	//(Don't fetch model on redirect!!)
			processed = true;
		}
		else if (view != null && view.hasRedirection()) {
			//TODO throw RedirectException instead
			processRedirect(response, view);
			processed = true;
		}
		else {
			log.warn("processView() - " + action.getClass().getName()+" produced no view and no redirection");
		}
		
		return processed;
	}
	
	protected void processJsp(HttpServletRequest request, HttpServletResponse response, View view, Map<String, Object> modelMap, Object action)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("processJsp() - forward the request to the given view: " + view.getJspFileName());
		}
		
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
		if (log.isDebugEnabled()) {
			log.debug("processRedirect() - initiate redirection to: " + view.getRedirection());
		}
		
		response.setHeader("Location", view.getRedirection().toExternalForm());
		response.setStatus(303);	//303 - "see other" (http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)
	}
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionViewProcessor#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Override
	public void setActionViewProcessorFactory(ActionViewProcessorFactory factory) {
		//
	}
}
