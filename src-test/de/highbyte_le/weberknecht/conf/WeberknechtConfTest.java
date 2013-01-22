/*
 * WeberknechtConfTest.java (weberknecht)
 *
 * Copyright 2010-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.conf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

import de.highbyte_le.weberknecht.request.processing.Processor;
import de.highbyte_le.weberknecht.request.routing.AreaPath;
import de.highbyte_le.weberknecht.request.routing.AreaPathResolver;

/**
 * Testing {@link WeberknechtConf}
 * 
 * @author pmairif
 */
public class WeberknechtConfTest {
	
	@Test
	public void testGetActionClassMap1() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		
		Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
		expectedActionClassMap.put("foo", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "pre1", "post1", null));
		expectedActionClassMap.put("bar", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "pre1", "post1", null));
		assertEquals(expectedActionClassMap, conf.getActionClassMap());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPreProcessorListMap1() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		
		Map<String, ProcessorList> expectedPre = new HashMap<String, ProcessorList>();
		List<Class<? extends Processor>> expectedPreProcessorClasses = new Vector<Class<? extends Processor>>();
		expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor1"));
		expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor2"));
		expectedPre.put("pre1", new ProcessorList("pre1", expectedPreProcessorClasses));
		
		assertEquals(expectedPre, conf.getPreProcessorListMap());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPostProcessorListMap1() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");

		Map<String, ProcessorList> expectedPost = new HashMap<String, ProcessorList>();
		List<Class<? extends Processor>> expectedPostProcessorClasses = new Vector<Class<? extends Processor>>();
		expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor3"));
		expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor4"));
		expectedPost.put("post1", new ProcessorList("post1", expectedPostProcessorClasses));
		
		assertEquals(expectedPost, conf.getPostProcessorListMap());
	}
	
	@Test
	public void testGetActionClassMap2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		
		{	//default area
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			expectedActionClassMap.put("foo", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "pre1", "post1", null));
			expectedActionClassMap.put("bar", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap());
		}
		
		{	//area a1
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			expectedActionClassMap.put("foo1", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "pre2", "post1", null));
			expectedActionClassMap.put("bar1", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "pre1", "post1", null));
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
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			expectedActionClassMap.put("foo", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "pre1", "post1", null));
			expectedActionClassMap.put("bar", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap());
		}
		
		{	//area a1
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			expectedActionClassMap.put("foo1", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "pre2", "post1", null));
			expectedActionClassMap.put("bar1", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "pre1", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1")));
		}			
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPreProcessorListMap2() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		
		Map<String, ProcessorList> expectedPre = new HashMap<String, ProcessorList>();

		{	//pre1
			List<Class<? extends Processor>> expectedPreProcessorClasses = new Vector<Class<? extends Processor>>();
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor2"));
			expectedPre.put("pre1", new ProcessorList("pre1", expectedPreProcessorClasses));
		}
		
		{	//pre2
			List<Class<? extends Processor>> expectedPreProcessorClasses = new Vector<Class<? extends Processor>>();
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor1"));
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor2"));
			expectedPre.put("pre2", new ProcessorList("pre2", expectedPreProcessorClasses));
		}
		
		assertEquals(expectedPre, conf.getPreProcessorListMap());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPostProcessorListMap2() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");

		Map<String, ProcessorList> expectedPost = new HashMap<String, ProcessorList>();

		//post1
		List<Class<? extends Processor>> expectedPostProcessorClasses = new Vector<Class<? extends Processor>>();
		expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor3"));
		expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor4"));
		expectedPost.put("post1", new ProcessorList("post1", expectedPostProcessorClasses));
		
		assertEquals(expectedPost, conf.getPostProcessorListMap());
	}
	
	@Test
	public void testFindActionDeclaration2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		//referred processors
		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals("pre1", declaration.getPreProcessorSet());
		assertEquals("post1", declaration.getPostProcessorSet());

		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals("", declaration.getPreProcessorSet());
		assertEquals("post1", declaration.getPostProcessorSet());

		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals("pre2", declaration.getPreProcessorSet());
		assertEquals("post1", declaration.getPostProcessorSet());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals("pre1", declaration.getPreProcessorSet());
		assertEquals("post1", declaration.getPostProcessorSet());
		
		assertNull(resolver.getActionDeclaration(new AreaPath("a1"), "foo"));
		assertNull(resolver.getActionDeclaration(new AreaPath("a2"), "foo"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPreProcessorListMap3() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-3.xml");
		
		Map<String, ProcessorList> expectedPre = new HashMap<String, ProcessorList>();

		{	//p1
			List<Class<? extends Processor>> expectedPreProcessorClasses = new Vector<Class<? extends Processor>>();
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor1"));
			expectedPre.put("p1", new ProcessorList("p1", expectedPreProcessorClasses));
		}
		
		{	//p2
			List<Class<? extends Processor>> expectedPreProcessorClasses = new Vector<Class<? extends Processor>>();
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor2"));
			expectedPreProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor3"));
			expectedPre.put("p2", new ProcessorList("p2", expectedPreProcessorClasses));
		}
		
		assertEquals(expectedPre, conf.getPreProcessorListMap());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPostProcessorListMap3() throws IOException, ConfigurationException, ClassNotFoundException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-3.xml");

		Map<String, ProcessorList> expectedPost = new HashMap<String, ProcessorList>();

		{	//p1
			List<Class<? extends Processor>> expectedPostProcessorClasses = new Vector<Class<? extends Processor>>();
			expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor1"));
			expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor2"));
			expectedPost.put("p1", new ProcessorList("p1", expectedPostProcessorClasses));
		}

		{	//p2
			List<Class<? extends Processor>> expectedPostProcessorClasses = new Vector<Class<? extends Processor>>();
			expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor3"));
			expectedPost.put("p2", new ProcessorList("p2", expectedPostProcessorClasses));
		}
		
		{	//p3
			List<Class<? extends Processor>> expectedPostProcessorClasses = new Vector<Class<? extends Processor>>();
			expectedPostProcessorClasses.add((Class<? extends Processor>)Class.forName("de.highbyte_le.weberknecht.test.DummyProcessor4"));
			expectedPost.put("p3", new ProcessorList("p3", expectedPostProcessorClasses));
		}
		
		assertEquals(expectedPost, conf.getPostProcessorListMap());
	}
	
	@Test
	public void testFindActionDeclaration3() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-3.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		//referred processors
		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals("p1", declaration.getPreProcessorSet());
		assertEquals("p1", declaration.getPostProcessorSet());

		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals("", declaration.getPreProcessorSet());
		assertEquals("", declaration.getPostProcessorSet());

		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals("p1", declaration.getPreProcessorSet());
		assertEquals("p2", declaration.getPostProcessorSet());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals("p2", declaration.getPreProcessorSet());
		assertEquals("p3", declaration.getPostProcessorSet());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar2");
		assertEquals("p2", declaration.getPreProcessorSet());
		assertEquals("p2", declaration.getPostProcessorSet());
		
		assertNull(resolver.getActionDeclaration(new AreaPath("a1"), "foo"));
		assertNull(resolver.getActionDeclaration(new AreaPath("a2"), "foo"));
	}
	
	@Test
	public void testErrorHandler() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-5.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler1", declaration.getErrorHandlerClass());

		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler3", declaration.getErrorHandlerClass());

		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
	}
	
	@Test
	public void testErrorHandlerWithSubAreas1() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-6a.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler1", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler3", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());

		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "foo2");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());

		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "bar2");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
	}
	
	@Test
	public void testErrorHandlerWithSubAreas2() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-6b.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler1", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler3", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "foo2");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2", "a3"), "foo3");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "bar2");
		assertEquals("de.highbyte_le.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
	}
	
	@Test
	public void testGetSubAreas() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-7.xml");
		
		{	// root
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			expectedActionClassMap.put("foo", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "pre1", "post1", null));
			expectedActionClassMap.put("bar", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "pre1", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath()));
		}
		{	// /a1
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			expectedActionClassMap.put("foo", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction1", "pre2", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1")));
		}
		{	// /a1/a2a
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			expectedActionClassMap.put("foo", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction2", "pre2", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1", "a2a")));
		}
		{	// /a1/a2b
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1", "a2b")));
		}
		{	// /a1/a2c
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			expectedActionClassMap.put("bar", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction2", "pre2", "post2", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1", "a2c")));
		}
		{	// /a1/a2a/a3
			Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
			expectedActionClassMap.put("foo", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction3", "pre1", "post1", null));
			assertEquals(expectedActionClassMap, conf.getActionClassMap(new AreaPath("a1", "a2a", "a3")));
		}
	}
	
	/**
	 * Exception on old configuration
	 */
	@Test(expected=ConfigurationException.class)
	public void testReadConfigOld() throws IOException, ConfigurationException {
		readConfig("test-data/weberknecht-old.xml");
	}

	/**
	 * Exception on corrupt configuration
	 */
	@Test(expected=ConfigurationException.class)
	public void testReadConfigCorrupt1() throws IOException, ConfigurationException {
		readConfig("test-data/weberknecht-corrupt-1.xml");
	}
	
	/**
	 * Exception on corrupt configuration
	 */
	@Test(expected=ConfigurationException.class)
	public void testReadConfigCorrupt2() throws IOException, ConfigurationException {
		readConfig("test-data/weberknecht-corrupt-2.xml");
	}
	
	/**
	 * pre processor does not implement the processor interface
	 */
	@Test(expected=ConfigurationException.class)
	public void testReadConfigCorrupt3() throws IOException, ConfigurationException {
		readConfig("test-data/weberknecht-corrupt-3.xml");
	}
	
	/**
	 * pre processor class does not exist
	 */
	@Test(expected=ConfigurationException.class)
	public void testReadConfigCorrupt4() throws IOException, ConfigurationException {
		readConfig("test-data/weberknecht-corrupt-4.xml");
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
