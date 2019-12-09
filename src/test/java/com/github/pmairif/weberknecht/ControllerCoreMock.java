/*
 * ControllerCoreMock.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht;

import javax.servlet.ServletContext;

import com.github.pmairif.weberknecht.conf.ActionDeclaration;
import com.github.pmairif.weberknecht.conf.ConfigurationException;
import com.github.pmairif.weberknecht.conf.WeberknechtConf;
import com.github.pmairif.weberknecht.db.DbConnectionProvider;
import com.github.pmairif.weberknecht.request.error.ErrorHandler;

/**
 * overriding the methods that generate error handlers.
 */
public class ControllerCoreMock extends ControllerCore {
	
	private ErrorHandler defaultErrHandler = null;
	private ErrorHandler customErrHandler = null;

	protected ControllerCoreMock(ServletContext servletContext, WeberknechtConf conf,
			DbConnectionProvider dbConnectionProvider) throws ClassNotFoundException, ConfigurationException {
		super(servletContext, conf, dbConnectionProvider);
	}

	/* (non-Javadoc)
	 * @see ControllerCore#getCustomErrorHandler(ActionDeclaration)
	 */
	@Override
	protected ErrorHandler getCustomErrorHandler(ActionDeclaration actionDeclaration) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		return this.customErrHandler;
	}
	
	/**
	 * @param customErrHandler the customErrHandler to set
	 */
	public void setCustomErrHandler(ErrorHandler customErrHandler) {
		this.customErrHandler = customErrHandler;
	}
	
	/* (non-Javadoc)
	 * @see ControllerCore#getDefaultErrorHandler()
	 */
	@Override
	protected ErrorHandler getDefaultErrorHandler() throws InstantiationException, IllegalAccessException {
		return this.defaultErrHandler;
	}
	
	/**
	 * @param defaultErrHandler the defaultErrHandler to set
	 */
	public void setDefaultErrHandler(ErrorHandler defaultErrHandler) {
		this.defaultErrHandler = defaultErrHandler;
	}
}
