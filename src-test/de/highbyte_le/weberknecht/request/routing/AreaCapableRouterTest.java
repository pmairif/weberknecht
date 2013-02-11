/*
 * AreaCapableRouterTest.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import static de.highbyte_le.weberknecht.test.TestUtil.readConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;

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

		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		router.setConfig(conf);
	}

	@Test
	public void testEmpty() {
		RoutingTarget target = router.routeUri("");
		assertNull(target);
	}
	
	@Test
	public void simpleTest1() {
		RoutingTarget target = router.routeUri("/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void simpleTest2() {
		RoutingTarget target = router.routeUri("/bar.data");
		assertEquals("bar", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void areaTest1() {
		RoutingTarget target = router.routeUri("/area/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("area"), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void areaTest2() {
		RoutingTarget target = router.routeUri("/Foo1/bar.data");
		assertEquals("bar", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals(new AreaPath("Foo1"), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void subAreaTest1() {
		RoutingTarget target = router.routeUri("/Area/Sub/Action.do");
		assertEquals("Action", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("Area", "Sub"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void subAreaTest2() {
		RoutingTarget target = router.routeUri("/a1/a2/a3/a4/a5/Action.do");
		assertEquals("Action", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2", "a3", "a4", "a5"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void subAreaTest3() {
		RoutingTarget target = router.routeUri("/foo/bar/foo/bar.do");
		assertEquals("bar", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("foo", "bar", "foo"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void subAreaTest4() {
		RoutingTarget target = router.routeUri("/a1//a2/bar.do");
		assertEquals("bar", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void testWithUnderscore() {
		RoutingTarget target = router.routeUri("/bar_foo.data");
		assertEquals("bar_foo", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void testWithDash() {
		RoutingTarget target = router.routeUri("/bar-foo!add-one.data");
		assertEquals("bar-foo", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals("add-one", target.getTask());
		assertEquals(new AreaPath(), target.getAreaPath());
	}

	@Test
	public void testWithTask() {
		RoutingTarget target1 = router.routeUri("/foo!sth.do");
		assertEquals("foo", target1.getActionName());
		assertEquals("do", target1.getViewProcessorName());
		assertEquals("sth", target1.getTask());
		assertEquals(new AreaPath(), target1.getAreaPath());

		RoutingTarget target2 = router.routeUri("/foo!bar.do");
		assertEquals("foo", target2.getActionName());
		assertEquals("do", target2.getViewProcessorName());
		assertEquals("bar", target2.getTask());
		assertEquals(new AreaPath(), target2.getAreaPath());
	}

	@Test
	public void testWithTaskAndArea() {
		RoutingTarget target1 = router.routeUri("/foo-bar/foo!sth.do");
		assertEquals("foo", target1.getActionName());
		assertEquals("do", target1.getViewProcessorName());
		assertEquals("sth", target1.getTask());
		assertEquals(new AreaPath("foo-bar"), target1.getAreaPath());
		
		RoutingTarget target2 = router.routeUri("/fooo/foo!bar.do");
		assertEquals("foo", target2.getActionName());
		assertEquals("do", target2.getViewProcessorName());
		assertEquals("bar", target2.getTask());
		assertEquals(new AreaPath("fooo"), target2.getAreaPath());
	}
	
	@Test
	public void testWithEmptyTask() {
		RoutingTarget target = router.routeUri("/foo!.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getTask());
	}
	
	@Test
	public void testDefaultAction1() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-1.xml");
		router.setConfig(conf);

		{	//expect default action
			RoutingTarget target = router.routeUri("/");
			assertEquals("foo", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
		}
		
		{	//we want at least a slash
			RoutingTarget target = router.routeUri("");
			assertNull(target);
		}
		
		{	//no default action declared
			RoutingTarget target = router.routeUri("/a1/");
			assertNull(target);
		}
	}

	@Test
	public void testDefaultAction2() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-2.xml");
		router.setConfig(conf);
		
		{	//no default action declared
			RoutingTarget target = router.routeUri("/");
			assertNull(target);
		}
		
		{	//expect default action
			RoutingTarget target = router.routeUri("/a1/");
			assertEquals("bar1", target.getActionName());
			assertEquals("data", target.getViewProcessorName());
			assertNull(target.getTask());
		}
		
		{	//without trailing slash
			RoutingTarget target = router.routeUri("/a1");
			assertEquals("bar1", target.getActionName());
			assertEquals("data", target.getViewProcessorName());
			assertNull(target.getTask());
		}
	}

	@Test
	public void testDefaultAction3() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-3.xml");
		router.setConfig(conf);
		
		{	//expect default action
			RoutingTarget target = router.routeUri("/");
			assertEquals("bar1", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertEquals("foo", target.getTask());
		}
	}
	
	@Test
	public void testDefaultAction4() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-4.xml");
		router.setConfig(conf);
		
		{	//expect default action
			RoutingTarget target = router.routeUri("/");
			assertEquals("bar1", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
		}
	}
}
