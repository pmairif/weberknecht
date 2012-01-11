/*
 * Controller.java
 *
 * Copyright 2008-2011 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: Jan 22, 2008
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.db.DBConnectionException;
import de.highbyte_le.weberknecht.db.DbConnectionProvider;
import de.highbyte_le.weberknecht.db.DefaultWebDbConnectionProvider2;
import de.highbyte_le.weberknecht.request.actions.ActionExecutionException;
import de.highbyte_le.weberknecht.request.actions.ActionFactory;
import de.highbyte_le.weberknecht.request.actions.ActionInstantiationException;
import de.highbyte_le.weberknecht.request.actions.ActionNotFoundException;
import de.highbyte_le.weberknecht.request.actions.ActionViewProcessor;
import de.highbyte_le.weberknecht.request.actions.ActionViewProcessorFactory;
import de.highbyte_le.weberknecht.request.actions.DynamicActionFactory;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.processing.ActionExecution;
import de.highbyte_le.weberknecht.request.processing.ProcessingChain;
import de.highbyte_le.weberknecht.request.processing.ProcessingException;
import de.highbyte_le.weberknecht.request.processing.Processor;
import de.highbyte_le.weberknecht.request.routing.Router;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;
import de.highbyte_le.weberknecht.request.routing.SimpleRouter;

/**
 * webapp controller
 * 
 * @author pmairif
 */
@SuppressWarnings({ "nls", "serial" })
public class Controller extends HttpServlet {
	
	private DbConnectionProvider mainDbConnectionProvider = null;
	
	private Map<String, DbConnectionProvider> additionalDbConnectionProviderMap = new HashMap<String, DbConnectionProvider>();
	
	/**
	 * mapping of areas to action factories
	 */
	private Map<String, ActionFactory> actionFactoryMap = null;
	
	private ActionViewProcessorFactory actionProcessorFactory = null;

	private List<Processor> processors = new Vector<Processor>();
	
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

			WeberknechtConf conf = WeberknechtConf.readConfig(getServletContext());
			
			// choose router depending on config
			this.router = createRouter(conf);
			
			//actions
			this.actionFactoryMap = createActionFactoryMap(conf);
			
			//action processors
			actionProcessorFactory = new ActionViewProcessorFactory();
			//TODO register action processors from config
			
			//TODO read generic processing chain from config
			//pre processors
			for (String pp: conf.getPreProcessorClasses()) {
				Object o = Class.forName(pp).newInstance();
				if (o instanceof Processor)
					processors.add((Processor)o);
				else
					log.error(pp+" is not an instance of Processor");
			}
			
			processors.add(new ActionExecution());
			
			//post processors
			for (String pp: conf.getPostProcessorClasses()) {
				Object o = Class.forName(pp).newInstance();
				if (o instanceof Processor)
					processors.add((Processor)o);
				else
					log.error(pp+" is not an instance of Processor");
			}
			
			//TODO add view processor
			

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

	private Map<String, ActionFactory> createActionFactoryMap(WeberknechtConf conf) {
		Map<String, ActionFactory> map = new HashMap<String, ActionFactory>();
		
		Set<String> areas = conf.getAreas();
		for (String area: areas) {
			ActionFactory factory = new DynamicActionFactory();
			map.put(area, factory);
			
			for (Entry<String, String> e: conf.getActionClassMap(area).entrySet()) {
				factory.registerAction(e.getKey(), e.getValue());
			}
		}
		
		return map;
	}

	private Router createRouter(WeberknechtConf conf) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		
		Router ret = null;
		
		String routerClass = conf.getRouterClass();
		if (routerClass != null) {
			Object o = Class.forName(routerClass).newInstance();
			if (o instanceof Router)
				ret = (Router) o;
			else
				log.error(routerClass + " is not an instance of Router");
		}
		
		if (ret == null)
			ret = new SimpleRouter();
		
		return ret;
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
		try {
			RoutingTarget routingTarget = router.routeUri(request.getServletPath());

			ModelHelper modelHelper = new ModelHelper(request, getServletContext());
			modelHelper.setSelf(constructSelf(request));
			
			ExecutableAction action = getAction(routingTarget);
			if (log.isDebugEnabled())
				log.debug("doGet() - processing action "+action.getClass().getSimpleName());

			//main db connection
			Connection con = null;
			if (isDbConnectionNeeded(action)) {
				con = getConnection();
				connectionList.add(con);
			}

			//initialization
			for (Processor p: processors) {
				if (p.needsDatabase())
					p.setDatabase(con);
			}
			
			connectionList.addAll( initializeAction(action, con) );

			//processing
			ProcessingChain chain = new ProcessingChain(processors, request, response, routingTarget, action);
			chain.doContinue();
			
			//process view
			ActionViewProcessor processor = actionProcessorFactory.createActionProcessor(routingTarget.getViewProcessorName());	//TODO implement as processor
			processor.setServletContext(getServletContext());
			processor.processView(request, response, action);
			
		}
		catch (ActionNotFoundException e) {
			log.warn("action not found: "+e.getMessage()+"; request URI was "+request.getRequestURI());
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);	//throw 404, if action doesn't exist
		}
		catch (ContentProcessingException e) {
			log.error("doGet() - ContentProcessingException: "+e.getMessage());	//$NON-NLS-1$
			response.setStatus(e.getHttpStatusCode());
			//TODO write the error message to output 
		}
		catch (ActionInstantiationException e) {
			log.warn("action could not instantiated: "+e.getMessage(), e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//throw 500, if action could not instantiated
		}
		catch (ProcessingException e) {
			log.error("doGet() - PreProcessingException: "+e.getMessage(), e);	//$NON-NLS-1$
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//throw 500
		}
		catch (DBConnectionException e) {
			log.error("doGet() - DBConnectionException: "+e.getMessage(), e);	//$NON-NLS-1$
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//throw 500
		}
		catch (ActionExecutionException e) {
			log.error("doGet() - ActionExecutionException: "+e.getMessage(), e);	//$NON-NLS-1$
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	//throw 500
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
	
	private String constructSelf(HttpServletRequest request) {	//FIXME this really needs to be tested!
		StringBuilder b = new StringBuilder();
		
		b.append(request.getContextPath());
		b.append(request.getServletPath());
		
		return b.toString();
	}
	
	private ExecutableAction getAction(RoutingTarget routingTarget) throws ActionNotFoundException, ActionInstantiationException {
		String action1 = routingTarget.getActionName();
		if (action1 == null)
			throw new ActionNotFoundException();
		
		ActionFactory actionFactory = actionFactoryMap.get(routingTarget.getArea());
		if (null == actionFactory)
			throw new ActionNotFoundException();
		
		ExecutableAction a = actionFactory.createAction(action1);
		if (null == a)
			throw new ActionNotFoundException();

		if (log.isDebugEnabled())
			log.debug("getAction() - action is "+a.getClass().getSimpleName());

		return a;
	}

	/**
	 * checks, if we need a DB connection
	 */
	private boolean isDbConnectionNeeded(ExecutableAction action) {
		boolean need = false;
		
		if (action instanceof DatabaseCapable && ((DatabaseCapable)action).needsDatabase())
			need = true;
		
		if (!need) {
			for (Processor pp: processors) {
				if (pp.needsDatabase()) {
					need = true;
					break;
				}
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
}
