/*
 * ActionExecution.java (weberknecht)
 *
 * Copyright 2010-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.processing;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.i18n.Localizable;
import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.CookieSetting;
import de.highbyte_le.weberknecht.request.ExecutionException;
import de.highbyte_le.weberknecht.request.NotFoundException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.actions.TaskedExecutableAction;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * Executes an {@link ExecutableAction}.
 * 
 * @author pmairif
 */
public class ActionExecution implements Processor {
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.processing.Processor#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.actions.ExecutableAction)
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, RoutingTarget routingTarget,
			ExecutableAction action, ProcessingChain chain)
            throws ExecutionException, ContentProcessingException, RedirectException, NotFoundException {
		
		try {
			//preparation
			if (action instanceof Localizable) {
				((Localizable) action).setRequestedLocale(routingTarget.getLocale());
			}

			//execution
			if (action instanceof TaskedExecutableAction) {
				((TaskedExecutableAction) action).execute(request, routingTarget.getTask());
			}
			else {
				action.execute(request);
			}

			//cookies
			if (action instanceof CookieSetting) {
				for (Cookie cookie: ((CookieSetting) action).getCookies()) {
					response.addCookie(cookie);
				}
			}

			chain.doContinue();
		}
		catch (IOException e) {
			throw new ProcessingException("i/o exception: "+e.getMessage(), e);
		}
	}
}
