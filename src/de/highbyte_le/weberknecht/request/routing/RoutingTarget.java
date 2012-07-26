/*
 * Routing.java (weberknecht)
 *
 * Copyright 2011 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 09.12.2011
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

/**
 * Routing target
 *
 * @author rick
 */
public class RoutingTarget {
	
	/**
	 * optional area
	 */
	private final String area;
	
	/**
	 * unique name identifying the action
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
	
	public RoutingTarget(String area, String actionName, String viewProcessorName, String task) {
		this.area = area == null ? "" : area;
		this.actionName = actionName;
		this.viewProcessorName = viewProcessorName;
		this.task = task;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
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
}
