/*
 * AreaPathResolver.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.conf.ActionDeclaration;
import de.highbyte_le.weberknecht.conf.ConfigurationException;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.request.actions.ActionFactory;
import de.highbyte_le.weberknecht.request.actions.ActionInstantiationException;
import de.highbyte_le.weberknecht.request.actions.ActionNotFoundException;
import de.highbyte_le.weberknecht.request.actions.DynamicActionFactory;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;

/**
 * resolve paths
 *
 * @author pmairif
 */
public class AreaPathResolver {

	//TODO matching only on valid actions

	/**
	 * Logger for this class
	 */
	private final Log log = LogFactory.getLog(AreaPathResolver.class);

	/**
	 * mapping of areas to action factories
	 */
	private final Map<AreaPath, ActionFactory> actionFactoryMap;
	
	private final WeberknechtConf conf;
	
	public AreaPathResolver(WeberknechtConf conf) throws ConfigurationException {
		this.actionFactoryMap = createActionFactoryMap(conf);
		this.conf = conf;
	}
	
	public ExecutableAction resolveAction(RoutingTarget routingTarget) throws ActionNotFoundException, ActionInstantiationException {
		if (null == routingTarget)
			throw new ActionNotFoundException();

		String actionName = routingTarget.getActionName();
		if (actionName == null)
			throw new ActionNotFoundException();
		
		ActionFactory actionFactory = actionFactoryMap.get(routingTarget.getAreaPath());
		if (null == actionFactory)
			throw new ActionNotFoundException();
		
		ExecutableAction action = actionFactory.createAction(actionName);
		if (null == action)
			throw new ActionNotFoundException();
		
		if (log.isDebugEnabled())
			log.debug("resolveAction() - action is "+action.getClass().getSimpleName());

		return action;
	}
	
	/**
	 * Is the target (path and action) known?
	 * @param routingTarget
	 * @return true, if the target can be resolved to a known action.
	 */
	public boolean knownTarget(RoutingTarget routingTarget) {
		if (null == routingTarget)
			return false;
		
		ActionFactory actionFactory = actionFactoryMap.get(routingTarget.getAreaPath());
		if (null == actionFactory)
			return false;
		
		return actionFactory.knowsAction(routingTarget.getActionName());
	}
	
	/**
	 * get the matching action declaration
	 */
	public ActionDeclaration getActionDeclaration(RoutingTarget routingTarget) {
		if (null == routingTarget)
			return null;
		
		AreaPath path = routingTarget.getAreaPath();
		String actionName = routingTarget.getActionName();
		return getActionDeclaration(path, actionName);
	}
	
	public ActionDeclaration getActionDeclaration(AreaPath path, String actionName) {
		Map<String, ActionDeclaration> actionClassMap = conf.getAreaActionClassMap().get(path);
		if (actionClassMap == null)
			return null;
		return actionClassMap.get(actionName);
	}
	
	private Map<AreaPath, ActionFactory> createActionFactoryMap(WeberknechtConf conf) throws ConfigurationException {
		try {
			Map<AreaPath, ActionFactory> map = new HashMap<AreaPath, ActionFactory>();
			
			Set<AreaPath> areas = conf.getAreas();
			for (AreaPath area: areas) {
				ActionFactory factory = new DynamicActionFactory();
				map.put(area, factory);
				
				for (Entry<String, ActionDeclaration> e: conf.getActionClassMap(area).entrySet()) {
					factory.registerAction(e.getKey(), e.getValue().getClazz());
				}
			}
			
			return map;
		}
		catch (ActionInstantiationException e) {
			throw new ConfigurationException(e.getMessage(), e);
		}
	}
}
