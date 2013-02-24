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

	@Override
	public String toString() {
		return "RoutingTarget [areaPath=" + areaPath + ", actionName=" + actionName + ", viewProcessorName="
				+ viewProcessorName + ", task=" + task + ", locale=" + locale + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + ((areaPath == null) ? 0 : areaPath.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		result = prime * result + ((viewProcessorName == null) ? 0 : viewProcessorName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoutingTarget other = (RoutingTarget) obj;
		if (actionName == null) {
			if (other.actionName != null)
				return false;
		}
		else if (!actionName.equals(other.actionName))
			return false;
		if (areaPath == null) {
			if (other.areaPath != null)
				return false;
		}
		else if (!areaPath.equals(other.areaPath))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		}
		else if (!locale.equals(other.locale))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		}
		else if (!task.equals(other.task))
			return false;
		if (viewProcessorName == null) {
			if (other.viewProcessorName != null)
				return false;
		}
		else if (!viewProcessorName.equals(other.viewProcessorName))
			return false;
		return true;
	}
}
