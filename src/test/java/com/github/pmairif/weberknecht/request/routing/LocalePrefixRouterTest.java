/*
 * LocalePrefixRouterTest.java (weberknecht)
 *
 * Copyright 2011-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import static com.github.pmairif.weberknecht.test.TestUtil.readConfig;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.github.pmairif.weberknecht.conf.ConfigurationException;
import com.github.pmairif.weberknecht.conf.WeberknechtConf;

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
		RoutingTarget target = router.process("");
		assertNull(target);
	}
	
	@Test
	public void simpleTest1() {
		RoutingTarget target = router.process("/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void simpleTestEmptySuffix() {
		RoutingTarget target = router.process("/foo");
		assertNotNull(target);
		assertEquals("foo", target.getActionName());
		assertEquals("", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void simpleTest2() {
		RoutingTarget target = router.process("/bar.data");
		assertEquals("bar", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void unknownActionTest1() {
		assertNull(router.process("/abc.do"));
	}

	@Test
	public void unknownActionTest2() {
		assertNull(router.process("/abc/foo.do"));
	}
	
	@Test
	public void areaTest1() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		router.setConfig(conf);

		RoutingTarget target = router.process("/a1/foo1.do");
		assertEquals("foo1", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1"), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void areaTest2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		router.setConfig(conf);

		RoutingTarget target = router.process("/a1/foo1.data");
		assertEquals("foo1", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals(new AreaPath("a1"), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void subAreaTest1() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-7.xml");
		router.setConfig(conf);

		RoutingTarget target = router.process("/a1/a2a/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2a"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void subAreaTest2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-7.xml");
		router.setConfig(conf);

		RoutingTarget target = router.process("/a1/a2a/a3/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2a", "a3"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void subAreaTest3() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target = router.process("/a1/a2a/a1/bar.do");
		assertEquals("bar", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2a", "a1"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void subAreaTest4() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target = router.process("/a1//a2a/foo.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new AreaPath("a1", "a2a"), target.getAreaPath());
		assertNull(target.getTask());
	}
	
	@Test
	public void testWithUnderscore() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target = router.process("/bar_foo.data");
		assertEquals("bar_foo", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals(new AreaPath(), target.getAreaPath());
		assertNull(target.getTask());
	}

	@Test
	public void testWithDash() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target = router.process("/bar-foo!add-one.data");
		assertEquals("bar-foo", target.getActionName());
		assertEquals("data", target.getViewProcessorName());
		assertEquals("add-one", target.getTask());
		assertEquals(new AreaPath(), target.getAreaPath());
	}

	@Test
	public void testWithTask() {
		RoutingTarget target1 = router.process("/foo!sth.do");
		assertEquals("foo", target1.getActionName());
		assertEquals("do", target1.getViewProcessorName());
		assertEquals("sth", target1.getTask());
		assertEquals(new AreaPath(), target1.getAreaPath());

		RoutingTarget target2 = router.process("/foo!bar.do");
		assertEquals("foo", target2.getActionName());
		assertEquals("do", target2.getViewProcessorName());
		assertEquals("bar", target2.getTask());
		assertEquals(new AreaPath(), target2.getAreaPath());
	}

	@Test
	public void testWithTaskAndArea() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-9.xml");
		router.setConfig(conf);

		RoutingTarget target1 = router.process("/foo-bar/foo!sth.do");
		assertEquals("foo", target1.getActionName());
		assertEquals("do", target1.getViewProcessorName());
		assertEquals("sth", target1.getTask());
		assertEquals(new AreaPath("foo-bar"), target1.getAreaPath());
		
		RoutingTarget target2 = router.process("/fooo/foo!bar.do");
		assertEquals("foo", target2.getActionName());
		assertEquals("do", target2.getViewProcessorName());
		assertEquals("bar", target2.getTask());
		assertEquals(new AreaPath("fooo"), target2.getAreaPath());
	}
	
	@Test
	public void testWithEmptyTask() {
		RoutingTarget target = router.process("/foo!.do");
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getTask());
	}

	@Test
	public void testLocaleObligatoryWithLocaleDe() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.process("/de/index.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("index", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("de"), target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleObligatoryWithLocaleDeWithPath() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.process("/de/bar/foo.do");
		assertEquals(new AreaPath("bar"), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("de"), target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleObligatoryWithLocaleUs() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.process("/en_US/index.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("index", target.getActionName());
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
		
		RoutingTarget target = router.process("/foo.do");
		assertNull(target);

		target = router.process("/bar/foo.do");
		assertNull(target);
	}
	
	/**
	 * locale in path is not optional. if it is not valid, the path won't be resolved. 
	 */
	@Test
	public void testLocaleObligatoryWithWrongLocale() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.process("/fr/foo.do");
		assertNull(target);
	}
	
	@Test
	public void testLocaleOptionalWithLocaleDe() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.process("/de/index.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("index", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("de"), target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleOptionalWithLocaleCh() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.process("/de_CH/index.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("index", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("de", "CH"), target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleOptionalWithLocaleChWithPath() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.process("/de_CH/en/foo.do");
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
		
		RoutingTarget target = router.process("/index.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("index", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleOptionalWithoutLocaleWithPath() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.process("/bar/foo.do");
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
		
		RoutingTarget target = router.process("/fr/foo.do");
		assertEquals(new AreaPath("fr"), target.getAreaPath());
		assertEquals("foo", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertNull(target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testLocaleTrim() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-trim.xml");
		router.setConfig(conf);
		
		RoutingTarget target = router.process("/de/index.do");
		assertEquals(new AreaPath(), target.getAreaPath());
		assertEquals("index", target.getActionName());
		assertEquals("do", target.getViewProcessorName());
		assertEquals(new Locale("de"), target.getLocale());
		assertNull(target.getTask());
	}
	
	@Test
	public void testDefaultAction1() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-1.xml");
		router.setConfig(conf);

		{	//expect default action
			RoutingTarget target = router.process("/");
			assertEquals("foo", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
		}

		{	//no default action declared
			RoutingTarget target = router.process("/a1/");
			assertNull(target);
		}
		
		{	//no default action declared
			RoutingTarget target = router.process("/de/a1/");
			assertNull(target);
		}
	}

	/**
	 * second area with same name, inheriting default action
	 */
	@Test
	public void testDefaultAction5a() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-5.xml");
		router.setConfig(conf);
		
		{	//expect default action
			RoutingTarget target = router.process("/");
			assertEquals("foo", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
		}
	}
	
	@Test
	public void testDefaultAction5b() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-5.xml");
		router.setConfig(conf);
		
		{	//expect default action
			RoutingTarget target = router.process("/a1");
			assertEquals("bar", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
		}
	}
	
	@Test
	public void testDefaultAction1WithLocale() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-1-locale.xml");
		router.setConfig(conf);
		
		{	//expect default action
			RoutingTarget target = router.process("/");
			assertEquals("foo", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
		}
		
		{	//same with locale v1
			RoutingTarget target = router.process("/de/");
			assertEquals("foo", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
			assertEquals(new Locale("de"), target.getLocale());
		}
		
		{	//same with locale v2
			RoutingTarget target = router.process("/de");
			assertEquals("foo", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
			assertEquals(new Locale("de"), target.getLocale());
		}
		
		{	//no default action declared
			RoutingTarget target = router.process("/a1/");
			assertNull(target);
		}
		
		{	//no default action declared
			RoutingTarget target = router.process("/de/a1/");
			assertNull(target);
		}
	}
	
	@Test
	public void testDefaultAction2() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-2.xml");
		router.setConfig(conf);
		
		{	//no default action declared
			RoutingTarget target = router.process("/");
			assertNull(target);
		}
		
		{	//expect default action
			RoutingTarget target = router.process("/a1/");
			assertEquals("bar1", target.getActionName());
			assertEquals("data", target.getViewProcessorName());
			assertNull(target.getTask());
		}
		
		{	//without trailing slash
			RoutingTarget target = router.process("/a1");
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
			RoutingTarget target = router.process("/");
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
			RoutingTarget target = router.process("/");
			assertEquals("bar1", target.getActionName());
			assertEquals("do", target.getViewProcessorName());
			assertNull(target.getTask());
		}
	}
	
	/**
	 * make sure ambiguous paths are properly handled.
	 */
	@Test
	public void testNoSuffix1() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-no-suffix.xml");
		router.setConfig(conf);

		//expect action named foo in root area
		RoutingTarget expected = new RoutingTarget(new AreaPath(), "foo", "", null);
		assertEquals(expected, router.process("/foo"));
	}

	/**
	 * make sure ambiguous paths are properly handled.
	 */
	@Test
	public void testNoSuffix1b() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-no-suffix.xml");
		router.setConfig(conf);
		
		//expect action named foo in root area
		RoutingTarget expected = new RoutingTarget(new AreaPath(), "foo", "", null);
		assertEquals(expected, router.process("/foo/"));
	}
	
	/**
	 * make sure ambiguous paths are properly handled.
	 */
	@Test
	public void testNoSuffix2() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-no-suffix.xml");
		router.setConfig(conf);
		
		//'foo' meant as area
		RoutingTarget expected = new RoutingTarget(new AreaPath("foo"), "home", "", null);
		assertEquals(expected, router.process("/foo/home"));
	}
}
