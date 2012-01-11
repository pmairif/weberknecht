/*
 * AreaCapableRouterTest.java (weberknecht)
 *
 * Copyright 2011 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 09.12.2011
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * testing the {@link AreaCapableRouter}
 *
 * @author pmairif
 */
public class AreaCapableRouterTest {

	private AreaCapableRouter router;
	
	@Before
	public void setUp() throws Exception {
		router = new AreaCapableRouter();
	}

	@Test
	public void simpleTest1() {
		RoutingTarget target = router.routeUri("/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals("", target.getArea());
		assertNull(target.getTask());
	}

	@Test
	public void simpleTest2() {
		RoutingTarget target = router.routeUri("/bar.data");
		assertEquals("bar", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals("", target.getArea());
		assertNull(target.getTask());
	}

	@Test
	public void areaTest1() {
		RoutingTarget target = router.routeUri("/area/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals("area", target.getArea());
		assertNull(target.getTask());
	}

	@Test
	public void areaTest2() {
		RoutingTarget target = router.routeUri("/Foo1/bar.data");
		assertEquals("bar", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals("Foo1", target.getArea());
		assertNull(target.getTask());
	}

	@Test
	public void testWithUnderscore() {
		RoutingTarget target = router.routeUri("/bar_foo.data");
		assertEquals("bar_foo", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals("", target.getArea());
		assertNull(target.getTask());
	}

	@Test
	public void testWithDash() {
		RoutingTarget target = router.routeUri("/bar-foo!add-one.data");
		assertEquals("bar-foo", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals("add-one", target.getTask());
		assertEquals("", target.getArea());
	}

	@Test
	public void testWithTask() {
		RoutingTarget target1 = router.routeUri("/foo!sth.do");
		assertEquals("foo", target1.getActionName());
		assertEquals("do", target1.getViewProcessorName());
		assertEquals("sth", target1.getTask());
		assertEquals("", target1.getArea());

		RoutingTarget target2 = router.routeUri("/foo!bar.do");
		assertEquals("foo", target2.getActionName());
		assertEquals("do", target2.getViewProcessorName());
		assertEquals("bar", target2.getTask());
		assertEquals("", target2.getArea());
	}

	@Test
	public void testWithTaskAndArea() {
		RoutingTarget target1 = router.routeUri("/foo-bar/foo!sth.do");
		assertEquals("foo", target1.getActionName());
		assertEquals("do", target1.getViewProcessorName());
		assertEquals("sth", target1.getTask());
		assertEquals("foo-bar", target1.getArea());
		
		RoutingTarget target2 = router.routeUri("/fooo/foo!bar.do");
		assertEquals("foo", target2.getActionName());
		assertEquals("do", target2.getViewProcessorName());
		assertEquals("bar", target2.getTask());
		assertEquals("fooo", target2.getArea());
	}
	
	@Test
	public void testWithEmptyTask() {
		RoutingTarget target = router.routeUri("/foo!.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getTask());
	}
}
