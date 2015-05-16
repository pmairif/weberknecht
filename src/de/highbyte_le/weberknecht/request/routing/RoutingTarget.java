/*
 * RoutingTarget.java (weberknecht)
 *
 * Copyright 2011-2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import de.highbyte_le.weberknecht.request.routing.flex.Route;

import java.util.Locale;

/**
 * Routing target
 *
 * @author pmairif
 */
public class RoutingTarget {
	
	/**
	 * unique action identifier
	 */
	private final ActionPath actionPath;
	
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
        this.actionPath = new ActionPath(path, actionName);
		this.viewProcessorName = viewProcessorName;
		this.task = task;
		this.locale = locale;
	}

	public RoutingTarget(ActionPath actionPath, String viewProcessorName, String task, Locale locale) {
        this.actionPath = actionPath;
		this.viewProcessorName = viewProcessorName;
		this.task = task;
		this.locale = locale;
	}

    public RoutingTarget(Route route, Locale locale) {
        this(route.getActionPath(), route.getProcessor(), route.getTask(), locale);
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

    public ActionPath getActionPath() {
        return actionPath;
    }

    /**
	 * @return the area
	 */
	public AreaPath getAreaPath() {
		return actionPath.getAreaPath();
	}
	
	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionPath.getActionName();
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
        final StringBuilder sb = new StringBuilder("RoutingTarget{");
        sb.append("actionPath=").append(actionPath);
        sb.append(", viewProcessorName='").append(viewProcessorName).append('\'');
        sb.append(", task='").append(task).append('\'');
        sb.append(", locale=").append(locale);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoutingTarget)) return false;

        RoutingTarget that = (RoutingTarget) o;

        if (!actionPath.equals(that.actionPath)) return false;
        if (viewProcessorName != null ? !viewProcessorName.equals(that.viewProcessorName) : that.viewProcessorName != null)
            return false;
        if (task != null ? !task.equals(that.task) : that.task != null) return false;
        return !(locale != null ? !locale.equals(that.locale) : that.locale != null);

    }

    @Override
    public int hashCode() {
        int result = actionPath.hashCode();
        result = 31 * result + (viewProcessorName != null ? viewProcessorName.hashCode() : 0);
        result = 31 * result + (task != null ? task.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        return result;
    }
}
