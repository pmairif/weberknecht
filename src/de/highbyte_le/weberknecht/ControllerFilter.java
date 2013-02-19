/*
 * ControllerFilter.java
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.conf.ActionDeclaration;
import de.highbyte_le.weberknecht.conf.ProcessorList;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.db.DBConnectionException;
import de.highbyte_le.weberknecht.db.DbConnectionHolder;
import de.highbyte_le.weberknecht.db.DbConnectionProvider;
import de.highbyte_le.weberknecht.db.DefaultWebDbConnectionProvider2;
import de.highbyte_le.weberknecht.request.DatabaseCapable;
import de.highbyte_le.weberknecht.request.ModelHelper;
import de.highbyte_le.weberknecht.request.actions.ActionNotFoundException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.error.DefaultErrorHandler;
import de.highbyte_le.weberknecht.request.error.ErrorHandler;
import de.highbyte_le.weberknecht.request.processing.ActionExecution;
import de.highbyte_le.weberknecht.request.processing.ProcessingChain;
import de.highbyte_le.weberknecht.request.processing.Processor;
import de.highbyte_le.weberknecht.request.processing.RedirectException;
import de.highbyte_le.weberknecht.request.routing.AreaCapableRouter;
import de.highbyte_le.weberknecht.request.routing.AreaPathResolver;
import de.highbyte_le.weberknecht.request.routing.MetaRouter;
import de.highbyte_le.weberknecht.request.routing.Router;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;
import de.highbyte_le.weberknecht.request.view.ActionViewProcessor;
import de.highbyte_le.weberknecht.request.view.ActionViewProcessorFactory;
import de.highbyte_le.weberknecht.request.view.AutoViewProcessor;

/**
 * webapp controller using servlet filter mechanism
 * 
 * @author pmairif
 */
@SuppressWarnings({ "nls" })
public class ControllerFilter implements Filter {	//TODO join with Controller
	
	private DbConnectionProvider mainDbConnectionProvider = null;
	
	private AreaPathResolver pathResolver;
	
	private ActionViewProcessorFactory actionProcessorFactory = null;
	
	private WeberknechtConf conf;
	
	private ServletContext servletContext; //TODO check, if servlet context can be shared across requests
	
	/**
	 * Logger for this class
	 */
	private final Log log = LogFactory.getLog(ControllerFilter.class);

	/**
	 * filter initialization
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			WeberknechtConf conf = WeberknechtConf.readConfig(filterConfig.getServletContext());
			ServletContext servletContext = filterConfig.getServletContext();
			
			//main DB
			DbConnectionProvider dbConnectionProvider = null;
			try {
				dbConnectionProvider = new DefaultWebDbConnectionProvider2("jdbc/mydb");
			}
			catch (NamingException e) {
				if (log.isInfoEnabled())
					log.info("init() - jdbc/mydb not configured ("+e.getMessage()+")");	//$NON-NLS-1$
			}
			
			init(conf, servletContext, dbConnectionProvider);
		}
		catch (Exception e) {
			log.error("init() - Exception: "+e.getMessage(), e);
			throw new ServletException("internal error", e);
		}
	}
	
	protected void init(WeberknechtConf conf, ServletContext servletContext, DbConnectionProvider dbConnectionProvider) throws ClassNotFoundException {
		this.conf = conf;
		this.servletContext = servletContext;
		
		//actions
		this.pathResolver = new AreaPathResolver(conf);
		
		//action processors
		actionProcessorFactory = new ActionViewProcessorFactory();
		//register action processors from config
		for (Entry<String, String> e: conf.getActionProcessorSuffixMap().entrySet()) {
			actionProcessorFactory.registerProcessor(e.getKey(), e.getValue());
		}
		
		this.mainDbConnectionProvider = dbConnectionProvider;
	}

	Router createRouter(WeberknechtConf conf, DbConnectionHolder conHolder) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DBConnectionException {
		
		List<String> routerClasses = conf.getRouterClasses();
		List<Router> routers = new Vector<Router>(routerClasses.size());
		for (String routerClass: routerClasses) {
			Object o = Class.forName(routerClass).newInstance();
			if (o instanceof Router) {
				Router router = (Router) o;
				initializeObject(router, conHolder);
				routers.add(router);
			}
			else
				log.error(routerClass + " is not an instance of Router");
		}
		
		Router ret = null;
		int size = routers.size();
		if (size == 0)
			ret = new AreaCapableRouter();
		else if (size == 1)
			ret = routers.get(0);
		else
			ret = new MetaRouter(routers);

		ret.setConfig(conf);
		
		return ret;
	}
	
	/**
	 * create instances of processor list
	 * 
	 * @param processorList
	 * @return list of instantiated processors
	 */
	private List<Processor> instantiateProcessorList(ProcessorList processorList)
			throws InstantiationException, IllegalAccessException {
		
		List<Class<? extends Processor>> processorClasses = processorList.getProcessorClasses();
		List<Processor> processors = new Vector<Processor>(processorClasses.size());
		
		for (Class<? extends Processor> pp: processorClasses) {
			processors.add(pp.newInstance());
		}
		
		return processors;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		if (log.isDebugEnabled())
			log.debug("doFilter() - start");
		
		long start = System.currentTimeMillis();
		
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
			throw new IllegalArgumentException("no http request");
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		DbConnectionHolder conHolder = new DbConnectionHolder(mainDbConnectionProvider);
		try {
			Router router = createRouter(conf, conHolder);	//choose router depending on config
			RoutingTarget routingTarget = router.routeUri(httpRequest.getServletPath(), httpRequest.getPathInfo());
			if (null == routingTarget) {
				filterChain.doFilter(request, response);
			}
			else {
				executeAction(httpRequest, httpResponse, conHolder, routingTarget);

				long finish = System.currentTimeMillis();
				if (log.isInfoEnabled()) {
					log.info("service() - page delivery of '"+httpRequest.getRequestURI()+"' took "+(finish-start)+" ms");
				}
			}
		}
		catch (ActionNotFoundException e) {
			//TODO eleganter wäre es schon, wenn der router endgültig entscheidet, ob es die action gibt oder nicht 
			filterChain.doFilter(request, response);
		}
		catch (Exception e1) {
			try {
				log.error("service() - exception while error handler instantiation: "+e1.getMessage(), e1);	//$NON-NLS-1$
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//call error page 500, TODO wird das vielleicht eh gemacht?
			}
			catch (IOException e) {
				log.error("service() - IOException: "+e.getMessage(), e);	//$NON-NLS-1$
				httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//just return 500
			}
		}
		finally {
			try {
				conHolder.close();
			}
			catch (SQLException e) {
				log.error("service() - SQLException while closing db connection: "+e.getMessage());	//$NON-NLS-1$
			}
		}
	}

	private void executeAction(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			DbConnectionHolder conHolder, RoutingTarget routingTarget) throws ActionNotFoundException {
		try {

			ModelHelper modelHelper = new ModelHelper(httpRequest, servletContext);
			modelHelper.setSelf(httpRequest);
			
			ExecutableAction action = pathResolver.resolveAction(routingTarget);
			if (log.isDebugEnabled())
				log.debug("service() - processing action "+action.getClass().getSimpleName());

			List<Processor> processors = setupProcessors(routingTarget);
			
			//initialization
			for (Processor p: processors)
				initializeObject(p, conHolder);
			
			initializeObject(action, conHolder);

			//processing
			try {
				ProcessingChain chain = new ProcessingChain(processors, httpRequest, httpResponse, routingTarget, action);
				chain.doContinue();
				
				//process view
				//TODO implement as processor
				ActionViewProcessor processor = actionProcessorFactory.createActionProcessor(routingTarget.getViewProcessorName(), servletContext); 
				processor.processView(httpRequest, httpResponse, action);
			}
			catch (RedirectException e) {
				doRedirect(httpRequest, httpResponse, e.getLocalRedirectDestination());
			}
		}
		catch (ActionNotFoundException e) {
			throw e;
		}
		catch (Exception e) {
			handleException(httpRequest, httpResponse, routingTarget, e);
		}
	}
	
	protected List<Processor> setupProcessors(RoutingTarget routingTarget) throws InstantiationException, IllegalAccessException {
		List<Processor> processors = new Vector<Processor>();

		ActionDeclaration actionDeclaration = pathResolver.getActionDeclaration(routingTarget);
		
		//pre processors
		if (actionDeclaration != null) {
			ProcessorList processorList = conf.getPreProcessorListMap().get(actionDeclaration.getPreProcessorSet());
			if (processorList != null)
				processors.addAll(instantiateProcessorList(processorList));
		}
		
		processors.add(new ActionExecution());
		
		//post processors
		if (actionDeclaration != null) {
			ProcessorList processorList = conf.getPostProcessorListMap().get(actionDeclaration.getPostProcessorSet());
			if (processorList != null)
				processors.addAll(instantiateProcessorList(processorList));
		}

		return processors;
	}
	
	/**
	 * do initialization stuff here.
	 * 
	 * @param action	the action instance, processor or whatever to be initialized
	 * @param conHolder		holds database connection
	 */
	protected void initializeObject(Object action, DbConnectionHolder conHolder) throws DBConnectionException {
		log.debug("initializeAction()");
		
		if (action instanceof DatabaseCapable) {
			log.debug("setting action database");
			((DatabaseCapable)action).setDatabase(conHolder.getConnection());
		}

		//TODO irgendwie mit FilterConfig lösen
//		if (action instanceof Configurable)
//			((Configurable)action).setContext(getServletConfig(), servletContext);
	}

	private void doRedirect(HttpServletRequest request, HttpServletResponse response, String redirectDestination)
			throws MalformedURLException {
		URL reqURL = new URL(request.getRequestURL().toString());
		URL dest = new URL(reqURL, redirectDestination);
		response.setHeader("Location", dest.toExternalForm());
		response.setStatus(303);	//303 - "see other" (http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)
	}

	@SuppressWarnings("unchecked")
	protected void handleException(HttpServletRequest request, HttpServletResponse response,
			RoutingTarget routingTarget, Exception exception) {
		DbConnectionHolder dbConHolder = new DbConnectionHolder(mainDbConnectionProvider); 
		try {
			//get error handler
			Class<? extends ErrorHandler> errHandlerClass = DefaultErrorHandler.class;
			ActionDeclaration actionDeclaration = pathResolver.getActionDeclaration(routingTarget);
			if (actionDeclaration != null && actionDeclaration.hasErrorHandlerClass())
				errHandlerClass = (Class<? extends ErrorHandler>) Class.forName(actionDeclaration.getErrorHandlerClass());
			
			ErrorHandler handler = errHandlerClass.newInstance();
			
			//initialize error handler
			initializeObject(handler, dbConHolder);

			//handle exception
			handler.handleException(exception, request, routingTarget);
			
			//process view, respecting requested content type
			AutoViewProcessor processor = new AutoViewProcessor();
			processor.setServletContext(servletContext);
			processor.setActionViewProcessorFactory(actionProcessorFactory);
			boolean view = processor.processView(request, response, handler);

			//status
			int status = handler.getStatus();
			if (status > 0)	{//Don't set status, eg. on redirects
				if (view)
					response.setStatus(status);
				else
					response.sendError(status);
			}

		}
		catch (Exception e1) {
			try {
				log.error("handleException() - exception while error handler instantiation: "+e1.getMessage(), e1);	//$NON-NLS-1$
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//call error page 500
			}
			catch (IOException e) {
				log.error("handleException() - IOException: "+e.getMessage(), e);	//$NON-NLS-1$
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//just return 500
			}
		}
		finally {
			try {
				dbConHolder.close();
			}
			catch (SQLException e) {
				log.error("SQLException while closing db connection: "+e.getMessage());	//$NON-NLS-1$
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		//
	}
}
