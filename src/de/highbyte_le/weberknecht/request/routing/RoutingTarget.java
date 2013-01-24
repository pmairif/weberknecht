/*
 * RoutingTarget.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.Locale;


/**
 * Routing target
 *
 * @author pmairif
 */
public class RoutingTarget {
	
	/**
	 * path to the action
	 */
	private final AreaPath areaPath;
	
	/**
	 * name identifying the action - must be unique within path
	 */
	private final String actionName;
	
	/**
	 * unique name identifying the view processor (formerly called suffix)
	 */
	private final String viewProcessorName;
	
	/**
	 * optional task
	 */
	private final String task;
	
	/**
	 * requested locale (via routing-locale-prefix, optional)
	 */
	private final Locale locale;
	
	public RoutingTarget(AreaPath path, String actionName, String viewProcessorName, String task, Locale locale) {
		this.areaPath = path;
		this.actionName = actionName;
		this.viewProcessorName = viewProcessorName;
		this.task = task;
		this.locale = locale;
	}
	
	public RoutingTarget(AreaPath path, String actionName, String viewProcessorName, String task) {
		this(path, actionName, viewProcessorName, task, null);
	}
	
	public RoutingTarget(String area, String actionName, String viewProcessorName, String task) {
		this(new AreaPath(area), actionName, viewProcessorName, task);
	}
	
	public RoutingTarget(String actionName, String viewProcessorName, String task) {
		this(new AreaPath(), actionName, viewProcessorName, task);
	}
	
	public void addArea(String area) {
		this.areaPath.addPath(area);
	}
	
	/**
	 * @return the area
	 */
	public AreaPath getAreaPath() {
		return areaPath;
	}
	
	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @return the viewProcessorName
	 */
	public String getViewProcessorName() {
		return viewProcessorName;
	}
	
	/**
	 * @return the task
	 */
	public String getTask() {
		return task;
	}
	
	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}
}
