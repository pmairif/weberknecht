/*
 * ActionExecution.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 15.12.2010
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.processing;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.actions.ActionExecutionException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.actions.TaskedExecutableAction;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * Executes an {@link ExecutableAction}.
 * 
 * @author pmairif
 */
public class ActionExecution implements Processor {
	
	//NOTE future actions could be processors on its own. This is mainly for compatibility. 

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.processing.Processor#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.actions.ExecutableAction)
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, RoutingTarget routingTarget,
			ExecutableAction action, ProcessingChain chain)
	throws ProcessingException, ContentProcessingException {
		
		try {
			if (action instanceof TaskedExecutableAction)
				((TaskedExecutableAction)action).execute(request, response, routingTarget.getTask());
			else
				action.execute(request, response);
			
			chain.doContinue();
		}
		catch (ServletException e) {
			throw new ProcessingException("servlet exception: "+e.getMessage(), e);
		}
		catch (IOException e) {
			throw new ProcessingException("i/o exception: "+e.getMessage(), e);
		}
		catch (ActionExecutionException e) {
			throw new ProcessingException("action execution exception: "+e.getMessage(), e);
		}
	}
}
