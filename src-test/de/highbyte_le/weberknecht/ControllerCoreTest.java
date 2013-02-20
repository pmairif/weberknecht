/*
 * ControllerCoreTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import static de.highbyte_le.weberknecht.test.TestUtil.readConfig;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.db.DbConnectionHolder;
import de.highbyte_le.weberknecht.request.processing.ActionExecution;
import de.highbyte_le.weberknecht.request.processing.Processor;
import de.highbyte_le.weberknecht.request.routing.AreaCapableRouter;
import de.highbyte_le.weberknecht.request.routing.AreaPath;
import de.highbyte_le.weberknecht.request.routing.MetaRouter;
import de.highbyte_le.weberknecht.request.routing.Router;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;
import de.highbyte_le.weberknecht.request.routing.SimpleRouter;
import de.highbyte_le.weberknecht.test.DbUsingRouter;
import de.highbyte_le.weberknecht.test.DummyProcessor1;
import de.highbyte_le.weberknecht.test.DummyProcessor2;

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
		
		WeberknechtConf conf = readConfig("test-data/weberknecht-4.xml");
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
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		controller = new ControllerCore(servletContext, conf, null);
		Router router = controller.createRouter(conHolder);
		assertTrue(router instanceof AreaCapableRouter);
	}
	
	@Test
	public void testCreateRouter1() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-router1.xml");
		controller = new ControllerCore(servletContext, conf, null);
		Router router = controller.createRouter(conHolder);
		assertTrue(router instanceof SimpleRouter);
	}

	@Test
	public void testCreateRouter2() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-router2.xml");
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
		WeberknechtConf conf = readConfig("test-data/weberknecht-router3.xml");
		controller = new ControllerCore(servletContext, conf, null);
		Router router = controller.createRouter(conHolder);
		assertTrue(router instanceof DbUsingRouter);
		
		verify(conHolder, atLeast(1)).getConnection();
	}
}
