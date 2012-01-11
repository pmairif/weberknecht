/*
 * ReqUtil.java
 *
 * Copyright 2008-2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: Jan 22, 2008
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

import de.highbyte_le.weberknecht.request.routing.Router;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * @author pmairif
 * @deprecated replaced by {@link Router} and {@link RoutingTarget}.
 */
@Deprecated
public class ReqUtil extends RoutingTarget {
	public ReqUtil(String area, String actionName, String viewProcessorName, String task) {
		super(area, actionName, viewProcessorName, task);
	}

	/**
	 * @return the baseName
	 */
	public String getBaseName() {
		return super.getActionName();
	}
	
	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return super.getViewProcessorName();
	}
	
	public String getFullName() {
		StringBuilder b = new StringBuilder();
		
		String baseName = getBaseName();
		String suffix = getSuffix();
		String task = getTask();
		
		if (baseName != null && suffix != null) {
			b.append(baseName);
			
			if (task != null)
				b.append("!").append(task);
			
			b.append('.').append(suffix);
		}
		
		return b.toString();
	}
}
