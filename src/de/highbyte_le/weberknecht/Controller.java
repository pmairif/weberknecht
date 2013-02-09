/*
 * Controller.java
 *
 * Copyright 2008-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: Jan 22, 2008
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.conf.ActionDeclaration;
import de.highbyte_le.weberknecht.conf.ProcessorList;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.db.DBConnectionException;
import de.highbyte_le.weberknecht.db.DbConnectionProvider;
import de.highbyte_le.weberknecht.db.DefaultWebDbConnectionProvider2;
import de.highbyte_le.weberknecht.request.AdditionalDatabaseCapable;
import de.highbyte_le.weberknecht.request.Configurable;
import de.highbyte_le.weberknecht.request.DatabaseCapable;
import de.highbyte_le.weberknecht.request.ModelHelper;
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
	
	private Map<String, DbConnectionProvider> additionalDbConnectionProviderMap = new HashMap<String, DbConnectionProvider>();
	
	private AreaPathResolver pathResolver;
	
	private ActionViewProcessorFactory actionProcessorFactory = null;
	
	private WeberknechtConf conf;
	
	private Router router = null;
	
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
			conf = WeberknechtConf.readConfig(getServletContext());
			
			// choose router depending on config
			this.router = createRouter(conf);
			
			//actions
			this.pathResolver = new AreaPathResolver(conf);
			
			//action processors
			actionProcessorFactory = new ActionViewProcessorFactory();
			//register action processors from config
			for (Entry<String, String> e: conf.getActionProcessorSuffixMap().entrySet()) {
				actionProcessorFactory.registerProcessor(e.getKey(), e.getValue());
			}
			
			//main DB
			try {
				mainDbConnectionProvider = new DefaultWebDbConnectionProvider2("jdbc/mydb");
			}
			catch (NamingException e) {
				if (log.isInfoEnabled())
					log.info("init() - jdbc/mydb not configured ("+e.getMessage()+")");	//$NON-NLS-1$
			}

			//additional DBs
			for (String db: conf.getDbs()) {
				try {
					log.info("got db config for "+db);
					additionalDbConnectionProviderMap.put(db, new DefaultWebDbConnectionProvider2("jdbc/"+db));
				}
				catch (NamingException e) {
					if (log.isInfoEnabled())
						log.info("init() - jdbc/"+db+" not configured ("+e.getMessage()+")");	//$NON-NLS-1$
				}
			}
		}
		catch (Exception e) {
			log.error("init() - Exception: "+e.getMessage(), e);
			throw new ServletException("internal error", e);
		}
	}

	Router createRouter(WeberknechtConf conf) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		
		List<Router> routers = new Vector<Router>();
		List<String> routerClasses = conf.getRouterClasses();
		for (String routerClass: routerClasses) {
			Object o = Class.forName(routerClass).newInstance();
			if (o instanceof Router) {
				routers.add((Router) o);
			}
			else
				log.error(routerClass + " is not an instance of Router");
		}
		
		Router ret = null;
		if (routers.size() == 0)
			ret = new AreaCapableRouter();
		else if (routers.size() == 1)
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
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (log.isDebugEnabled())
			log.debug("doGet() - start");
		
		long start = System.currentTimeMillis();
		
		List<Connection> connectionList = new Vector<Connection>();
		RoutingTarget routingTarget = router.routeUri(request.getServletPath());
		try {

			ModelHelper modelHelper = new ModelHelper(request, getServletContext());
			modelHelper.setSelf(request);
			
			ExecutableAction action = pathResolver.resolveAction(routingTarget);
			if (log.isDebugEnabled())
				log.debug("doGet() - processing action "+action.getClass().getSimpleName());

			List<Processor> processors = setupProcessors(routingTarget);
			
			//main db connection
			Connection con = null;
			if (isDbConnectionNeeded(action, processors)) {
				con = getConnection();
				connectionList.add(con);
			}

			//initialization
			for (Processor p: processors) {
				if (p instanceof DatabaseCapable)
					((DatabaseCapable) p).setDatabase(con);
			}
			
			connectionList.addAll( initializeAction(action, con) );

			//processing
			try {
				ProcessingChain chain = new ProcessingChain(processors, request, response, routingTarget, action);
				chain.doContinue();
				
				//process view
				//TODO implement as processor
				ActionViewProcessor processor = actionProcessorFactory.createActionProcessor(routingTarget.getViewProcessorName(), getServletContext()); 
				processor.processView(request, response, action);
			}
			catch (RedirectException e) {
				doRedirect(request, response, e.getLocalRedirectDestination());
			}
		}
		catch (Exception e) {
			handleException(request, response, routingTarget, e);
		}
		finally {
			for (Connection con: connectionList) {
				try {
					con.close();
				}
				catch (SQLException e) {
					log.error("SQLException while closing db connection: "+e.getMessage());	//$NON-NLS-1$
				}
			}
		}
		
		long finish = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("page delivery took "+(finish-start)+" ms");
		}
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
		Connection con = null;
		try {
			//get error handler
			Class<? extends ErrorHandler> errHandlerClass = DefaultErrorHandler.class;
			ActionDeclaration actionDeclaration = pathResolver.getActionDeclaration(routingTarget);
			if (actionDeclaration != null && actionDeclaration.hasErrorHandlerClass())
				errHandlerClass = (Class<? extends ErrorHandler>) Class.forName(actionDeclaration.getErrorHandlerClass());
			
			ErrorHandler handler = errHandlerClass.newInstance();
			
			//initialize error handler
			if (handler instanceof DatabaseCapable) {
				con = getConnection();
				((DatabaseCapable) handler).setDatabase(con);
			}
			if (handler instanceof Configurable)
				((Configurable)handler).setContext(getServletConfig(), getServletContext());

			//handle exception
			handler.handleException(exception, request, routingTarget);
			
			//process view, respecting requested content type
			AutoViewProcessor processor = new AutoViewProcessor();
			processor.setServletContext(getServletContext());
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
				if (con != null)
					con.close();
			}
			catch (SQLException e) {
				log.error("SQLException while closing db connection: "+e.getMessage());	//$NON-NLS-1$
			}
		}
	}
	
	/**
	 * checks, if we need a DB connection
	 */
	private boolean isDbConnectionNeeded(ExecutableAction action, List<Processor> processors) {
		if (action instanceof DatabaseCapable)
			return true;
		
		boolean need = false;
		for (Processor pp: processors) {
			if (pp instanceof DatabaseCapable) {
				need = true;
				break;
			}
		}
		
		return need;
	}

	/**
	 * do initialization stuff here.
	 * 
	 * @param action	the action instance to be initialized
	 * @param con		the main database connection
	 */
	protected List<Connection> initializeAction(ExecutableAction action, Connection con) {
		
		//TODO find a generic way to initialize processors and actions and make processors as well Configurable and AdditionalDatabaseCapable
		log.debug("initializeAction()");
		
		List<Connection> connectionList = new Vector<Connection>();
		
		if (action instanceof DatabaseCapable) {
			log.debug("setting action database");
			((DatabaseCapable)action).setDatabase(con);
		}

		//set additional db connections
		if (action instanceof AdditionalDatabaseCapable)	//TODO implement as processor?
			connectionList.addAll( initDbConnections( (AdditionalDatabaseCapable) action ) );
		
		if (action instanceof Configurable)
			((Configurable)action).setContext(getServletConfig(), getServletContext());

		return connectionList;
	}

	private List<Connection> initDbConnections(AdditionalDatabaseCapable action) {
		List<Connection> connectionList = new Vector<Connection>();
		
		for (Entry<String, DbConnectionProvider> e: additionalDbConnectionProviderMap.entrySet()) {
			String db = e.getKey();
			DbConnectionProvider prov = e.getValue();

			try {
				if (action.needsDatabase(db)) {
					Connection con = prov.getConnection();
					action.setDatabase(db, con);
					connectionList.add(con);
				}
			}
			catch (DBConnectionException e1) {
				log.error("initDbConnections() - DBConnectionException with '"+db+"': "+e1.getMessage(), e1);	//$NON-NLS-1$
			}
		}
		
		return connectionList;
	}
	
	/**
	 * get the main db connection
	 */
	protected Connection getConnection() throws DBConnectionException {
		if (null == mainDbConnectionProvider)
			throw new DBConnectionException("missing db configuration.");

		return mainDbConnectionProvider.getConnection();
	}
	
	protected Connection getConnection(String contectName) throws DBConnectionException {
		if (null == additionalDbConnectionProviderMap)
			throw new DBConnectionException("missing db configuration.");
		
		DbConnectionProvider prov =  additionalDbConnectionProviderMap.get(contectName);
		if (prov != null)
			return prov.getConnection();
		return null;
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
