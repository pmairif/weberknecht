/*
 * SimpleRouterTest.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * testing the {@link SimpleRouter}
 *
 * @author pmairif
 */
public class SimpleRouterTest {

	private SimpleRouter router;
	
	@Before
	public void setUp() throws Exception {
		router = new SimpleRouter();
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
	public void testWithUnderscore() {
		RoutingTarget util = router.routeUri("/bar_foo.data", null);
		assertEquals("bar_foo", util.getActionName());
		assertEquals("data", util.getViewProcessorName());
		assertNull(util.getTask());
	}

	@Test
	public void testWithDash() {
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
