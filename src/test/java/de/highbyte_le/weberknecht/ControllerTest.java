/*
 * ControllerTest.java (weberknecht)
 *
 * Copyright 2012-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import static de.highbyte_le.weberknecht.test.TestUtil.readConfig;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import de.highbyte_le.weberknecht.conf.ConfigurationException;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.test.MockAction;

/**
 * Testing Controller
 *
 * @author pmairif
 */
public class ControllerTest {
	
	private Controller controller;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext servletContext;

	@Before
	public void setUp() throws Exception {
		servletContext = mock(ServletContext.class);
		
		controller = new Controller();

		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		MockAction.setCallCount(0);
	}

	/**
	 * action is executed
	 */
	@Test
	public void testDoFilterActionExecuted() throws IOException, ServletException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-controller-test.xml");
		controller.init(servletContext, conf, null);

		when(request.getServletPath()).thenReturn("/foo.do");
		when(request.getPathInfo()).thenReturn(null);
		
		controller.service(request, response);
		
		//action was executed
		assertEquals(1, MockAction.getCallCount());
	}

	/**
	 * router found no target, no action executed
	 */
	@Test
	public void testDoFilterActionNotExecuted() throws IOException, ServletException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-controller-test.xml");
		controller.init(servletContext, conf, null);

		when(request.getServletPath()).thenReturn("/xyz");
		when(request.getPathInfo()).thenReturn(null);
		
		controller.service(request, response);
		
		//no action executed
		assertEquals(0, MockAction.getCallCount());
	}
}
