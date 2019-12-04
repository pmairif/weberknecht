/*
 * ControllerCoreTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pmairif.weberknecht.conf.WeberknechtConf;
import com.github.pmairif.weberknecht.db.DbConnectionHolder;
import com.github.pmairif.weberknecht.request.error.ErrorHandler;
import com.github.pmairif.weberknecht.request.processing.ActionExecution;
import com.github.pmairif.weberknecht.request.processing.Processor;
import com.github.pmairif.weberknecht.request.routing.AreaCapableRouter;
import com.github.pmairif.weberknecht.request.routing.AreaPath;
import com.github.pmairif.weberknecht.request.routing.MetaRouter;
import com.github.pmairif.weberknecht.request.routing.Router;
import com.github.pmairif.weberknecht.request.routing.RoutingTarget;
import com.github.pmairif.weberknecht.request.routing.SimpleRouter;
import com.github.pmairif.weberknecht.test.DbUsingRouter;
import com.github.pmairif.weberknecht.test.DummyProcessor1;
import com.github.pmairif.weberknecht.test.DummyProcessor2;
import com.github.pmairif.weberknecht.test.TestUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing Controller
 *
 * @author pmairif
 */
public class ControllerCoreTest {
	
	private ControllerCore controller;
	
	private DbConnectionHolder conHolder;
	
	private ServletContext servletContext;

	@Before
	public void setUp() throws Exception {
		conHolder = mock(DbConnectionHolder.class);
		
		WeberknechtConf conf = TestUtil.readConfig("test-data/weberknecht-4.xml");
		servletContext = mock(ServletContext.class);
		
		controller = new ControllerCore(servletContext, conf, null);
	}

	@Test
	public void testSetupProcessorsDefaultFoo() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Processor> processors = controller.setupProcessors(new RoutingTarget(new AreaPath(), "foo", null, null));
		
		//pre1 and post1 expected
		int i = 0;
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//pre1
		assertTrue(processors.get(i++) instanceof ActionExecution);	//action
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//post1
		assertTrue(processors.get(i++) instanceof DummyProcessor2);
	}
	
	@Test
	public void testSetupProcessorsDefaultBar() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Processor> processors = controller.setupProcessors(new RoutingTarget(new AreaPath(), "bar", null, null));
		
		//post1 expected
		int i = 0;
		assertTrue(processors.get(i++) instanceof ActionExecution);	//action
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//post1
		assertTrue(processors.get(i++) instanceof DummyProcessor2);
	}

	@Test
	public void testSetupProcessorsA1Foo1() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Processor> processors = controller.setupProcessors(new RoutingTarget(new AreaPath("a1"), "foo1", null, null));
		
		//pre2 and post1 expected
		int i = 0;
		assertTrue(processors.get(i++) instanceof DummyProcessor2);	//pre2
		assertTrue(processors.get(i++) instanceof ActionExecution);	//action
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//post1
		assertTrue(processors.get(i++) instanceof DummyProcessor2);
	}
	
	@Test
	public void testSetupProcessorsA1Bar1() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Processor> processors = controller.setupProcessors(new RoutingTarget(new AreaPath("a1"), "bar1", null, null));
		
		//pre1 and post1 expected
		int i = 0;
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//pre1
		assertTrue(processors.get(i++) instanceof ActionExecution);	//action
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//post1
		assertTrue(processors.get(i++) instanceof DummyProcessor2);
	}
	
	@Test
	public void testCreateRouterDefault() throws Exception {
		WeberknechtConf conf = TestUtil.readConfig("test-data/weberknecht-1.xml");
		controller = new ControllerCore(servletContext, conf, null);
		Router router = controller.createRouter(conHolder);
		assertTrue(router instanceof AreaCapableRouter);
	}
	
	@Test
	public void testCreateRouter1() throws Exception {
		WeberknechtConf conf = TestUtil.readConfig("test-data/weberknecht-router1.xml");
		controller = new ControllerCore(servletContext, conf, null);
		Router router = controller.createRouter(conHolder);
		assertTrue(router instanceof SimpleRouter);
	}

	@Test
	public void testCreateRouter2() throws Exception {
		WeberknechtConf conf = TestUtil.readConfig("test-data/weberknecht-router2.xml");
		controller = new ControllerCore(servletContext, conf, null);
		Router router = controller.createRouter(conHolder);
		assertTrue(router instanceof MetaRouter);
		MetaRouter metaRouter = (MetaRouter) router;
		List<Router> routers = metaRouter.getRouters();
		assertTrue(routers.get(0) instanceof SimpleRouter);
		assertTrue(routers.get(1) instanceof AreaCapableRouter);
		
		verify(conHolder, times(0)).getConnection();
	}

	@Test
	public void testCreateRouterWithDb() throws Exception {
		WeberknechtConf conf = TestUtil.readConfig("test-data/weberknecht-router3.xml");
		controller = new ControllerCore(servletContext, conf, null);
		Router router = controller.createRouter(conHolder);
		assertTrue(router instanceof DbUsingRouter);
		
		verify(conHolder, atLeast(1)).getConnection();
	}
	
	/**
	 * just the default error handler is called 
	 */
	@Test
	public void testHandleExceptionDefaultHandler() throws Exception {
		WeberknechtConf conf = TestUtil.readConfig("test-data/weberknecht-1.xml");
		ControllerCoreMock ctrl = new ControllerCoreMock(servletContext, conf, null);
		
		ErrorHandler defaultHandler = mock(ErrorHandler.class);
		ctrl.setDefaultErrHandler(defaultHandler);
		ErrorHandler customHandler = mock(ErrorHandler.class);
		ctrl.setCustomErrHandler(customHandler);

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		RoutingTarget routingTarget = new RoutingTarget("foo", "do", null);
		Exception exception = mock(Exception.class);
		
		ctrl.handleException(request, response, routingTarget, exception);
		
		verify(customHandler, never()).handleException(exception, request, routingTarget);
		verify(defaultHandler, times(1)).handleException(exception, request, routingTarget);
	}

	/**
 	 * custom error handler is called; no fall back to default error handler
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testHandleExceptionCustomHandler() throws Exception {
		WeberknechtConf conf = TestUtil.readConfig("test-data/weberknecht-custom-errhandler.xml");
		ControllerCoreMock ctrl = new ControllerCoreMock(servletContext, conf, null);
		
		ErrorHandler defaultHandler = mock(ErrorHandler.class);
		ctrl.setDefaultErrHandler(defaultHandler);
		ErrorHandler customHandler = mock(ErrorHandler.class);
		ctrl.setCustomErrHandler(customHandler);

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		RoutingTarget routingTarget = new RoutingTarget("foo", "do", null);
		Exception exception = mock(Exception.class);

		when(customHandler.handleException(exception, request, routingTarget)).thenReturn(true);
		
		ctrl.handleException(request, response, routingTarget, exception);
		
		verify(customHandler, times(1)).handleException(exception, request, routingTarget);
		verify(defaultHandler, never()).handleException(exception, request, routingTarget);
	}

	/**
	 * custom error handler is called; falls back to default error handler
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testHandleExceptionCustomHandlerFallback() throws Exception {
		WeberknechtConf conf = TestUtil.readConfig("test-data/weberknecht-custom-errhandler.xml");
		ControllerCoreMock ctrl = new ControllerCoreMock(servletContext, conf, null);
		
		ErrorHandler defaultHandler = mock(ErrorHandler.class);
		ctrl.setDefaultErrHandler(defaultHandler);
		ErrorHandler customHandler = mock(ErrorHandler.class);
		ctrl.setCustomErrHandler(customHandler);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		RoutingTarget routingTarget = new RoutingTarget("foo", "do", null);
		Exception exception = mock(Exception.class);

		when(customHandler.handleException(exception, request, routingTarget)).thenReturn(false);

		ctrl.handleException(request, response, routingTarget, exception);
		
		verify(customHandler, times(1)).handleException(exception, request, routingTarget);
		verify(defaultHandler, times(1)).handleException(exception, request, routingTarget);
	}
}
