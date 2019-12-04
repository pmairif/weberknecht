/*
 * ActionViewProcessorProcessor.java
 *
 * tabstop=4, charset=utf8
 */
package de.highbyte_le.weberknecht.request.view;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.ExecutionException;
import de.highbyte_le.weberknecht.request.ModelHelper;
import de.highbyte_le.weberknecht.request.NotFoundException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.processing.ProcessingChain;
import de.highbyte_le.weberknecht.request.processing.Processor;
import de.highbyte_le.weberknecht.request.processing.RedirectException;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * processor calling {@link ActionViewProcessor}s
 * @author pmairif
 */
public class ActionViewProcessorProcessor implements Processor {

	/**
	 * Logger for this class
	 */
	private final static Log log = LogFactory.getLog(ActionViewProcessorProcessor.class);

	private final ActionViewProcessorFactory actionProcessorFactory;
	
	private final ServletContext servletContext;
	
	public ActionViewProcessorProcessor(ActionViewProcessorFactory actionProcessorFactory, ServletContext servletContext) {
		this.actionProcessorFactory = actionProcessorFactory;
		this.servletContext = servletContext;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.processing.Processor#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.routing.RoutingTarget, de.highbyte_le.weberknecht.request.actions.ExecutableAction, de.highbyte_le.weberknecht.request.processing.ProcessingChain)
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, RoutingTarget routingTarget,
			ExecutableAction action, ProcessingChain chain) throws ExecutionException, ContentProcessingException,
            RedirectException, NotFoundException {

		try {
			if (log.isDebugEnabled()) {
				log.debug("execute() - processing view vor action " + action.getClass().getSimpleName());
			}
			request.setAttribute(ModelHelper.ACTION_KEY, action);

			ActionViewProcessor processor = actionProcessorFactory.createActionProcessor(
					routingTarget.getViewProcessorName(), servletContext
			);
            if (null == processor) {
				throw new NotFoundException("view " + routingTarget.getViewProcessorName() + " not available", request.getRequestURI());
			}

			processor.processView(request, response, action);
			
		}
		catch (ServletException e) {
			throw new ExecutionException("servlet exception: "+e.getMessage(), e);
		}
		catch (IOException e) {
			throw new ExecutionException("i/o exception: "+e.getMessage(), e);
		}
    }
}