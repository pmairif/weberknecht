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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.highbyte_le.weberknecht.conf.ConfigurationException;
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
		RoutingTarget target = router.routeUri("", null);
		assertNull(target);
	}
	
	@Test
	public void simpleTest1() {
		RoutingTarget target = router.routeUri("/foo.do", null);
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void simpleTest1b() {
		RoutingTarget target = router.routeUri("", "/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void simpleTest2() {
		RoutingTarget target = router.routeUri("/bar.data", null);
		assertEquals("bar", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
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
	public void areaTest1() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("/a1/foo1.do", null);
		assertEquals("foo1", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1"), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void areaTest1b() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("/a1", "/foo1.do");
		assertEquals("foo1", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void areaTest1c() throws ConfigurationException, IOException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("", "/a1/foo1.do");
		assertEquals("foo1", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void areaTest2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("/a1/foo1.data", null);
		assertEquals("foo1", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals(new AreaPath("a1"), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void subAreaTest1() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-7.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("/a1/a2a/foo.do", null);
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2a"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void subAreaTest2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-7.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("/a1/a2a/a3/foo.do", null);
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2a", "a3"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void subAreaTest3() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("/a1/a2a/a1/bar.do", null);
		assertEquals("bar", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2a", "a1"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void subAreaTest4() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("/a1//a2a/foo.do", null);
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2a"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void testWithUnderscore() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("/bar_foo.data", null);
		assertEquals("bar_foo", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void testWithDash() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target = router.routeUri("/bar-foo!add-one.data", null);
		assertEquals("bar-foo", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals("add-one", target.getTask());
		assertEquals(new AreaPath(), target.getAreaPath());
	}

	@Test
	public void testWithTask() {
		RoutingTarget target1 = router.routeUri("/foo!sth.do", null);
		assertEquals("foo", target1.getActionName());
		assertEquals("do", target1.getViewProcessorName());
		assertEquals("sth", target1.getTask());
		assertEquals(new AreaPath(), target1.getAreaPath());

		RoutingTarget target2 = router.routeUri("/foo!bar.do", null);
		assertEquals("foo", target2.getActionName());
		assertEquals("do", target2.getViewProcessorName());
		assertEquals("bar", target2.getTask());
		assertEquals(new AreaPath(), target2.getAreaPath());
	}

	@Test
	public void testWithTaskAndArea() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target1 = router.routeUri("/foo-bar/foo!sth.do", null);
		assertEquals("foo", target1.getActionName());
		assertEquals("do", target1.getViewProcessorName());
		assertEquals("sth", target1.getTask());
		assertEquals(new AreaPath("foo-bar"), target1.getAreaPath());
		
		RoutingTarget target2 = router.routeUri("/fooo/foo!bar.do", null);
		assertEquals("foo", target2.getActionName());
		assertEquals("do", target2.getViewProcessorName());
		assertEquals("bar", target2.getTask());
		assertEquals(new AreaPath("fooo"), target2.getAreaPath());
	}
	
	@Test
	public void testWithEmptyTask() {
		RoutingTarget target = router.routeUri("/foo!.do", null);
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getTask());
	}

	@Test
	public void testDefaultAction1() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-1.xml");
		router.setConfig(conf);

		{	//expect default action
			RoutingTarget target = router.routeUri("/", null);
			assertEquals("foo", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
		}
		
		{	//we want at least a slash
			RoutingTarget target = router.routeUri("", null);
			assertNull(target);
		}
		
		{	//no default action declared
			RoutingTarget target = router.routeUri("/a1/", null);
			assertNull(target);
		}
	}

	@Test
	public void testDefaultAction2() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-2.xml");
		router.setConfig(conf);
		
		{	//no default action declared
			RoutingTarget target = router.routeUri("/", null);
			assertNull(target);
		}
		
		{	//expect default action
			RoutingTarget target = router.routeUri("/a1/", null);
			assertEquals("bar1", target.getActionName());
			assertEquals("data", target.getViewProcessorName());
			assertNull(target.getTask());
		}
		
		{	//without trailing slash
			RoutingTarget target = router.routeUri("/a1", null);
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
			RoutingTarget target = router.routeUri("/", null);
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
			RoutingTarget target = router.routeUri("/", null);
			assertEquals("bar1", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
		}
	}
}
