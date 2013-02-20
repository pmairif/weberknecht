/*
 * Controller.java
 *
 * Copyright 2008-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.conf.ActionDeclaration;
import de.highbyte_le.weberknecht.conf.ConfigurationException;
import de.highbyte_le.weberknecht.conf.ProcessorList;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.db.DBConnectionException;
import de.highbyte_le.weberknecht.db.DbConnectionHolder;
import de.highbyte_le.weberknecht.db.DbConnectionProvider;
import de.highbyte_le.weberknecht.db.DefaultWebDbConnectionProvider2;
import de.highbyte_le.weberknecht.request.Configurable;
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
 * webapp controller
 * 
 * @author pmairif
 */
@SuppressWarnings({ "nls", "serial" })
public class Controller extends HttpServlet {
	
	private DbConnectionProvider mainDbConnectionProvider = null;
	
	private AreaPathResolver pathResolver;
	
	private ActionViewProcessorFactory actionProcessorFactory = null;
	
	private WeberknechtConf conf;
	
	private ServletContext servletContext; //TODO check, if servlet context can be shared across requests

	/**
	 * Logger for this class
	 */
	private final Log log = LogFactory.getLog(Controller.class);

	/**
	 * initialization of the controller
	 */
	@Override
	public void init() throws ServletException {
		try {
			ServletContext servletContext = getServletContext();
			WeberknechtConf conf = WeberknechtConf.readConfig(servletContext);
			
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
			ClassNotFoundException, DBConnectionException, ConfigurationException {
		
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
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (log.isDebugEnabled())
			log.debug("service() - start");
		
		long start = System.currentTimeMillis();
		
		DbConnectionHolder conHolder = new DbConnectionHolder(mainDbConnectionProvider);
		try {
			Router router = createRouter(conf, conHolder);	//choose router depending on config
			RoutingTarget routingTarget = router.routeUri(request.getServletPath(), request.getPathInfo());

			try {
				if (null == routingTarget)
					throw new ActionNotFoundException();
	
				ModelHelper modelHelper = new ModelHelper(request, servletContext);
				modelHelper.setSelf(request);
				
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
					ProcessingChain chain = new ProcessingChain(processors, request, response, routingTarget, action);
					chain.doContinue();
					
					//process view
					//TODO implement as processor
					ActionViewProcessor processor = actionProcessorFactory.createActionProcessor(routingTarget.getViewProcessorName(), servletContext); 
					processor.processView(request, response, action);
				}
				catch (RedirectException e) {
					doRedirect(request, response, e.getLocalRedirectDestination());
				}
			}
			catch (Exception e) {
				handleException(request, response, routingTarget, e);
			}
		}
		catch (Exception e1) {
			try {
				log.error("service() - exception while error handler instantiation: "+e1.getMessage(), e1);	//$NON-NLS-1$
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//call error page 500
			}
			catch (IOException e) {
				log.error("service() - IOException: "+e.getMessage(), e);	//$NON-NLS-1$
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//just return 500
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
		
		long finish = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("service() - page delivery took "+(finish-start)+" ms");
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
	protected void initializeObject(Object action, DbConnectionHolder conHolder) throws DBConnectionException, ConfigurationException {
		log.debug("initializeAction()");
		
		if (action instanceof DatabaseCapable) {
			log.debug("setting action database");
			((DatabaseCapable)action).setDatabase(conHolder.getConnection());
		}

		if (action instanceof Configurable)
			((Configurable)action).setContext(getServletConfig(), servletContext);
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

		DbConnectionHolder conHolder = new DbConnectionHolder(mainDbConnectionProvider);
		try {
			//get error handler
			Class<? extends ErrorHandler> errHandlerClass = DefaultErrorHandler.class;
			ActionDeclaration actionDeclaration = pathResolver.getActionDeclaration(routingTarget);
			if (actionDeclaration != null && actionDeclaration.hasErrorHandlerClass())
				errHandlerClass = (Class<? extends ErrorHandler>) Class.forName(actionDeclaration.getErrorHandlerClass());
			
			ErrorHandler handler = errHandlerClass.newInstance();
			
			//initialize error handler
			initializeObject(handler, conHolder);

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
				conHolder.close();
			}
			catch (SQLException e) {
				log.error("SQLException while closing db connection: "+e.getMessage());	//$NON-NLS-1$
			}
		}
	}
	
	/**
	 * get the main db connection
	 */
	protected Connection getConnection() throws DBConnectionException {
		if (null == mainDbConnectionProvider)
			throw new DBConnectionException("missing db configuration.");

		return mainDbConnectionProvider.getConnection();
	}
	
	/**
	 * for testing purposes only
	 */
	protected void setConf(WeberknechtConf conf) {
		this.conf = conf;
	}
	
	/**
	 * for testing purposes only
	 * @param pathResolver the pathResolver to set
	 */
	protected void setPathResolver(AreaPathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}
}
