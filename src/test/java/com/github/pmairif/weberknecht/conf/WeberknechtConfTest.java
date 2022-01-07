/*
 * WeberknechtConfTest.java (weberknecht)
 *
 * Copyright 2010-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.conf;

import com.github.pmairif.weberknecht.request.processing.Processor;
import com.github.pmairif.weberknecht.request.routing.AreaPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.*;

import static com.github.pmairif.weberknecht.test.TestUtil.readConfig;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing {@link WeberknechtConf}
 * 
 * @author pmairif
 */
class WeberknechtConfTest {
	
	@Test
	public void testGetActionClassMap1() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		
		Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
		expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre1", "post1", null));
		expectedActionClassMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "pre1", "post1", null));
		assertEquals(expectedActionClassMap, conf.getActionClassMap());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPreProcessorListMap1() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		
		Map<String, ProcessorList> expectedPre = new HashMap<String, ProcessorList>();
		List<Class<? extends Processor>> expectedPreProcessorClasses = new Vector<Class<? extends Processor>>();
		expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor1"));
		expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor2"));
		expectedPre.put("pre1", new ProcessorList("pre1", expectedPreProcessorClasses));
		
		assertEquals(expectedPre, conf.getPreProcessorListMap());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPostProcessorListMap1() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");

		Map<String, ProcessorList> expectedPost = new HashMap<String, ProcessorList>();
		List<Class<? extends Processor>> expectedPostProcessorClasses = new Vector<Class<? extends Processor>>();
		expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor3"));
		expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor4"));
		expectedPost.put("post1", new ProcessorList("post1", expectedPostProcessorClasses));
		
		assertEquals(expectedPost, conf.getPostProcessorListMap());
	}
	
	@Test
	public void testGetActionClassMap2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		
		{	//default area
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre1", "post1", null));
			expectedActionClassMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap());
		}
		
		{	//area a1
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre2", "post1", null));
			expectedActionClassMap.put("bar1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "pre1", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1")));
		}			
	}
	
	/**
	 * syntax with (older) actions elements
	 */
	@Test
	public void testGetActionClassMap2Actions() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2-actions.xml");
		
		{	//default area
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre1", "post1", null));
			expectedActionClassMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap());
		}
		
		{	//area a1
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre2", "post1", null));
			expectedActionClassMap.put("bar1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "pre1", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1")));
		}			
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPreProcessorListMap2() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		
		Map<String, ProcessorList> expectedPre = new HashMap<>();

		{	//pre1
			List<Class<? extends Processor>> expectedPreProcessorClasses = new ArrayList<>();
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor2"));
			expectedPre.put("pre1", new ProcessorList("pre1", expectedPreProcessorClasses));
		}
		
		{	//pre2
			List<Class<? extends Processor>> expectedPreProcessorClasses = new ArrayList<>();
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor1"));
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor2"));
			expectedPre.put("pre2", new ProcessorList("pre2", expectedPreProcessorClasses));
		}
		
		assertEquals(expectedPre, conf.getPreProcessorListMap());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPostProcessorListMap2() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");

		Map<String, ProcessorList> expectedPost = new HashMap<>();

		//post1
		List<Class<? extends Processor>> expectedPostProcessorClasses = new ArrayList<>();
		expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor3"));
		expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor4"));
		expectedPost.put("post1", new ProcessorList("post1", expectedPostProcessorClasses));
		
		assertEquals(expectedPost, conf.getPostProcessorListMap());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPreProcessorListMap3() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-3.xml");
		
		Map<String, ProcessorList> expectedPre = new HashMap<>();

		{	//p1
			List<Class<? extends Processor>> expectedPreProcessorClasses = new ArrayList<>();
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor1"));
			expectedPre.put("p1", new ProcessorList("p1", expectedPreProcessorClasses));
		}
		
		{	//p2
			List<Class<? extends Processor>> expectedPreProcessorClasses = new ArrayList<>();
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor2"));
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor3"));
			expectedPre.put("p2", new ProcessorList("p2", expectedPreProcessorClasses));
		}
		
		assertEquals(expectedPre, conf.getPreProcessorListMap());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPostProcessorListMap3() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-3.xml");

		Map<String, ProcessorList> expectedPost = new HashMap<>();

		{	//p1
			List<Class<? extends Processor>> expectedPostProcessorClasses = new ArrayList<>();
			expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor1"));
			expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor2"));
			expectedPost.put("p1", new ProcessorList("p1", expectedPostProcessorClasses));
		}

		{	//p2
			List<Class<? extends Processor>> expectedPostProcessorClasses = new ArrayList<>();
			expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor3"));
			expectedPost.put("p2", new ProcessorList("p2", expectedPostProcessorClasses));
		}
		
		{	//p3
			List<Class<? extends Processor>> expectedPostProcessorClasses = new ArrayList<>();
			expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("com.github.pmairif.weberknecht.test.DummyProcessor4"));
			expectedPost.put("p3", new ProcessorList("p3", expectedPostProcessorClasses));
		}
		
		assertEquals(expectedPost, conf.getPostProcessorListMap());
	}
	
	@Test
	public void testErrorHandlerWithSubAreas1() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-6a.xml");
		
		Map<AreaPath, Map<String, ActionDeclaration>> expected = new HashMap<>();
		
		Map<String, ActionDeclaration> rootMap = new HashMap<>();
		rootMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler1"));
		rootMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler3"));
		expected.put(new AreaPath(), rootMap);

		Map<String, ActionDeclaration> a1Map = new HashMap<>();
		a1Map.put("foo1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2"));
		a1Map.put("bar1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler2"));
		expected.put(new AreaPath("a1"), a1Map);

		Map<String, ActionDeclaration> a2Map = new HashMap<>();
		a2Map.put("foo2", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2"));
		a2Map.put("bar2", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler2"));
		expected.put(new AreaPath("a1", "a2"), a2Map);
		
		assertEquals(expected, conf.getAreaActionClassMap());
	}
	
	@Test
	public void testErrorHandlerWithSubAreas2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-6b.xml");
		
		Map<AreaPath, Map<String, ActionDeclaration>> expected = new HashMap<>();
		
		Map<String, ActionDeclaration> rootMap = new HashMap<>();
		rootMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler1"));
		rootMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler3"));
		expected.put(new AreaPath(), rootMap);

		Map<String, ActionDeclaration> a1Map = new HashMap<>();
		a1Map.put("foo1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2"));
		a1Map.put("bar1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler2"));
		expected.put(new AreaPath("a1"), a1Map);

		Map<String, ActionDeclaration> a2Map = new HashMap<>();
		a2Map.put("foo2", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2"));
		a2Map.put("bar2", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler2"));
		expected.put(new AreaPath("a1", "a2"), a2Map);
		
		Map<String, ActionDeclaration> a3Map = new HashMap<>();
		a3Map.put("foo3", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2"));
		expected.put(new AreaPath("a1", "a2", "a3"), a3Map);
		
		assertEquals(expected, conf.getAreaActionClassMap());
	}
	
	@Test
	public void testGetSubAreas() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-7.xml");
		
		{	// root
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre1", "post1", null));
			expectedActionClassMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "pre1", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath()));
		}
		{	// /a1
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction3", "pre2", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1")));
		}
		{	// /a1/a2a
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre2", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1", "a2a")));
		}
		{	// /a1/a2b
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1", "a2b")));
		}
		{	// /a1/a2c
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction3", "pre2", "post2", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1", "a2c")));
		}
		{	// /a1/a2a/a3
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "pre1", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1", "a2a", "a3")));
		}
	}
	
	/**
	 * unnamed sub areas
	 */
	@Test
	public void testNoNameSubs() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-no-name-sub.xml");
		
		Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
		expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre1", "post1", null));
		expectedActionClassMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "pre2", "post1", "com.github.pmairif.weberknecht.test.DummyErrorHandler"));
		assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath()));
	}
	
	/**
	 * unnamed sub areas, another example
	 */
	@Test
	public void testNoNameSubs2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-no-name-sub-2.xml");
		
		{	// root
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre1", "post1", null));
			expectedActionClassMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "pre2", "post1", "com.github.pmairif.weberknecht.test.DummyErrorHandler"));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath()));
		}
		
		{	// /sub
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
			expectedActionClassMap.put("foo1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction3", "pre3", "post1", null));
			expectedActionClassMap.put("bar1", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction4", "pre3", "post2", "com.github.pmairif.weberknecht.test.DummyErrorHandler2"));
			expectedActionClassMap.put("bar2", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction5", "pre3", "post2", "com.github.pmairif.weberknecht.test.DummyErrorHandler2"));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("sub")));
		}
	}

	/**
	 * distinct sub areas with same name
	 */
	@Test
	public void testDistinctSubs() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-distinct-sub.xml");
		
		Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<>();
		expectedActionClassMap.put("foo", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre1", "post1", null));
		expectedActionClassMap.put("bar", new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "pre2", "", "com.github.pmairif.weberknecht.test.DummyErrorHandler"));
		assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath()));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"test-data/weberknecht-old.xml",		//Exception on old configuration
			"test-data/weberknecht-corrupt-1.xml",	//Exception on corrupt configuration
			"test-data/weberknecht-corrupt-2.xml",	//Exception on corrupt configuration
			"test-data/weberknecht-corrupt-3.xml",	//pre processor does not implement the processor interface
			"test-data/weberknecht-corrupt-4.xml",	//pre processor class does not exist
	})
	void testReadConfigOld(String file) {
		assertThrows(ConfigurationException.class, () -> readConfig(file));
	}

	@Test
	public void testRouter() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-router.xml");
		List<String> expected = new ArrayList<>();
		expected.add("com.example.Router");
		assertEquals(expected, conf.getRouterClasses());
	}
	
	@Test
	public void testRouter2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-router2.xml");
		List<String> expected = new ArrayList<>();
		expected.add("com.github.pmairif.weberknecht.request.routing.SimpleRouter");
		expected.add("com.github.pmairif.weberknecht.request.routing.AreaCapableRouter");
		assertEquals(expected, conf.getRouterClasses());
	}
	
	@Test
	public void testNoRouter() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		assertEquals(0, conf.getRouterClasses().size());
	}
	
	@Test
	public void testLocalePrefix() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		
		RoutingLocalePrefix expected = new RoutingLocalePrefix(false, "de", "en", "en_US");
		assertEquals(expected, conf.getRoutingLocalePrefix());
	}
	
	@Test
	public void testLocalePrefixOptional() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
		
		RoutingLocalePrefix expected = new RoutingLocalePrefix(true, "de", "de_CH");
		assertEquals(expected, conf.getRoutingLocalePrefix());
	}
	
	@Test
	public void testNoLocalePrefix() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		assertNull(conf.getRoutingLocalePrefix());
	}
	
	@Test
	public void testGetDefaultAction1() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-1.xml");
		
		assertEquals("foo.do", conf.getDefaultAction(new AreaPath()));
		assertNull(conf.getDefaultAction(new AreaPath("a1")));
	}
	
	@Test
	public void testGetDefaultAction1a() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-1a.xml");
		
		assertEquals("foo.do", conf.getDefaultAction(new AreaPath()));
		assertNull(conf.getDefaultAction(new AreaPath("a1")));
	}
	
	@Test
	public void testGetDefaultAction2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-2.xml");
		
		assertNull(conf.getDefaultAction(new AreaPath()));
		assertEquals("bar1.data", conf.getDefaultAction(new AreaPath("a1")));
	}
	
	@Test
	public void testGetDefaultAction3() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-3.xml");
		
		assertEquals("bar1!foo.do", conf.getDefaultAction(new AreaPath()));
	}
	
	@Test
	public void testGetDefaultAction4() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-default-4.xml");
		
		assertEquals("bar1.do", conf.getDefaultAction(new AreaPath()));
	}

	@Test
	public void testGetActionProcessorSuffixMap() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-view-processors.xml");

		Map<String, String> expected = new HashMap<>();
		expected.put("", "com.github.pmairif.weberknecht.request.view.WebActionProcessor");
		expected.put("xml", "com.github.pmairif.weberknecht.request.view.DataActionProcessor");
		
		assertEquals(expected, conf.getActionProcessorSuffixMap());
	}
}
