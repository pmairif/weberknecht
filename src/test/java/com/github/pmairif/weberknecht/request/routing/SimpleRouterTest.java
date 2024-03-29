/*
 * SimpleRouterTest.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import com.github.pmairif.weberknecht.conf.ConfigurationException;
import com.github.pmairif.weberknecht.conf.WeberknechtConf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.pmairif.weberknecht.test.TestUtil.readConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * testing the {@link SimpleRouter}
 *
 * @author pmairif
 */
class SimpleRouterTest {

	private SimpleRouter router;
	
	@BeforeEach
	public void setUp() throws Exception {
		router = new SimpleRouter();
		
		//apply simple confg without locale prefix
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		router.setConfig(conf);
	}

	@Test
	public void simpleTest1() {
		RoutingTarget target = router.routeUri("/foo.do", null);
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getTask());
		assertEquals(new AreaPath(), target.getAreaPath());
	}

	@Test
	public void simpleTest1b() {
		RoutingTarget target = router.routeUri("", "/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getTask());
		assertEquals(new AreaPath(), target.getAreaPath());
	}
	
	@Test
	public void simpleTest2() {
		RoutingTarget util = router.routeUri("/bar.data", null);
		assertEquals("bar", util.getActionName());
		assertEquals("data", util.getViewProcessorName());
		assertNull(util.getTask());
	}

	@Test
	public void unknownActionTest1() {
		assertNull(router.routeUri("/abc.do", null));
	}

	@Test
	public void unknownActionTest2() {
		assertNull(router.routeUri("/abc/foo.do", null));
	}
	

	@Test
	public void testWithUnderscore() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget util = router.routeUri("/bar_foo.data", null);
		assertEquals("bar_foo", util.getActionName());
		assertEquals("data", util.getViewProcessorName());
		assertNull(util.getTask());
	}

	@Test
	public void testWithDash() throws ConfigurationException, IOException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget util = router.routeUri("/bar-foo!add-one.data", null);
		assertEquals("bar-foo", util.getActionName());
		assertEquals("data", util.getViewProcessorName());
		assertEquals("add-one", util.getTask());
	}

	@Test
	public void testWithTask() {
		RoutingTarget util1 = router.routeUri("/foo!sth.do", null);
		assertEquals("foo", util1.getActionName());
		assertEquals("do", util1.getViewProcessorName());
		assertEquals("sth", util1.getTask());
		
		RoutingTarget util2 = router.routeUri("/foo!bar.do", null);
		assertEquals("foo", util2.getActionName());
		assertEquals("do", util2.getViewProcessorName());
		assertEquals("bar", util2.getTask());
	}

	@Test
	public void testWithEmptyTask() {
		RoutingTarget util1 = router.routeUri("/foo!.do", null);
		assertEquals("foo", util1.getActionName());
		assertEquals("do", util1.getViewProcessorName());
		assertNull(util1.getTask());
	}
}
