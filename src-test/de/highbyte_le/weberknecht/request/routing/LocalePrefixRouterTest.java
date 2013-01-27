/*
 * LocalePrefixRouterTest.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import de.highbyte_le.weberknecht.conf.ConfigurationException;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;

/**
 * testing the {@link LocalePrefixRouter}
 *
 * @author pmairif
 */
public class LocalePrefixRouterTest {

	private LocalePrefixRouter router;
	
	@Before
	public void setUp() throws Exception {
		router = new LocalePrefixRouter();
		
		//apply simple confg without locale prefix
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
	public void testLocaleObligatoryWithLocaleDe() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/de/foo.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("de"), target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleObligatoryWithLocaleUs() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/en_US/foo.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("en", "US"), target.getLocale());
		assertNull(target.getTask());
	}
	
	/**
	 * locale in path is not optional. if it is not present, the path won't be resolved. 
	 */
	@Test
	public void testLocaleObligatoryWithoutLocale() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/foo.do");
		assertNull(target);
	}
	
	/**
	 * locale in path is not optional. if it is not valid, the path won't be resolved. 
	 */
	@Test
	public void testLocaleObligatoryWithWrongLocale() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/fr/foo.do");
		assertNull(target);
	}
	
	@Test
	public void testLocaleOptionalWithLocaleDe() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/de/foo.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("de"), target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleOptionalWithLocaleCh() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/de_CH/foo.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("de", "CH"), target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleOptionalWithLocaleChWithPath() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/de_CH/en/foo.do");
		assertEquals(new AreaPath("en"), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("de", "CH"), target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleOptionalWithoutLocale() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/foo.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleOptionalWithoutLocaleWithPath() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/bar/foo.do");
		assertEquals(new AreaPath("bar"), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getLocale());
		assertNull(target.getTask());
	}
	
	/**
	 * locale is optional. Wrong locales are interpreted as usual path entries.
	 */
	@Test
	public void testLocaleOptionalWithWrongLocale() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.routeUri("/fr/foo.do");
		assertEquals(new AreaPath("fr"), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testParseLocale() throws Exception {
		assertEquals(new Locale("de"), router.parseLocale("de"));
		assertEquals(new Locale("de"), router.parseLocale(" de "));
		assertEquals(new Locale("en"), router.parseLocale("en"));
		assertEquals(new Locale("en", "US"), router.parseLocale("en_US"));
		
		assertNull(router.parseLocale(""));
		assertNull(router.parseLocale("  "));
		assertNull(router.parseLocale(null));
	}
	
	private WeberknechtConf readConfig(String filename) throws IOException, ConfigurationException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(filename));
			return WeberknechtConf.readConfig(in);
		}
		finally {
			if (in != null)
				in.close();
		}
	}

}
