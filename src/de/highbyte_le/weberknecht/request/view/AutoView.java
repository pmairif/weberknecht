/*
 * AutoAction.java
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.view;

import de.highbyte_le.weberknecht.request.error.ErrorHandler;

/**
 * Executables implementing this interface have a say in choosing the used {@link ActionViewProcessor}.
 * The view processor is not determined by the suffix in the URL. The action chooses it on it's own.
 * {@link AutoView}s are processed by {@link AutoViewProcessor}.
 * This interface is implemented by {@link ErrorHandler}, but Actions are free to use it, too. You just
 * have to register the {@link AutoViewProcessor} at the {@link ActionViewProcessorFactory} with an
 * appropriate name (suffix).
 * 
 * @author pmairif
 */
public interface AutoView {
	/**
	 * get the view processor name to use to process the view of the action.
	 * The action must implement the corresponding interface to be able to deliver the content.
	 * @return the view processor name (suffix)
	 */
	public String getViewProcessorName();
}
