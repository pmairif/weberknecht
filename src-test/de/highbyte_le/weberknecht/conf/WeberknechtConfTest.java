/*
 * WeberknechtConfTest.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 24.03.2010
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.conf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

/**
 * Testing {@link WeberknechtConf}
 * 
 * @author pmairif
 */
public class WeberknechtConfTest {

	@Test
	public void testReadConfig1() throws IOException, ConfigurationException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File("test-data/weberknecht-1.xml"));
			WeberknechtConf conf = WeberknechtConf.readConfig(in);
			
			{
				Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
				expectedActionClassMap.put("foo", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "pre1", "post1"));
				expectedActionClassMap.put("bar", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "pre1", "post1"));
				assertEquals(expectedActionClassMap, conf.getActionClassMap());
			}

			{
				Map<String, ProcessorList> expectedPre = new HashMap<String, ProcessorList>();
				List<String> expectedPreProcessorClasses = new Vector<String>();
				expectedPreProcessorClasses.add("de.highbyte_le.weberknecht.PreProcessor1");
				expectedPreProcessorClasses.add("de.highbyte_le.weberknecht.PreProcessor2");
				expectedPre.put("pre1", new ProcessorList("pre1", expectedPreProcessorClasses));
				
				assertEquals(expectedPre, conf.getPreProcessorListMap());
			}

			{
				Map<String, ProcessorList> expectedPost = new HashMap<String, ProcessorList>();
				List<String> expectedPostProcessorClasses = new Vector<String>();
				expectedPostProcessorClasses.add("de.highbyte_le.weberknecht.PostProcessor1");
				expectedPostProcessorClasses.add("de.highbyte_le.weberknecht.PostProcessor2");
				expectedPost.put("post1", new ProcessorList("post1", expectedPostProcessorClasses));
				
				assertEquals(expectedPost, conf.getPostProcessorListMap());
			}
		}
		finally {
			if (in != null)
				in.close();
		}
	}
	
	@Test
	public void testReadConfig2() throws IOException, ConfigurationException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File("test-data/weberknecht-2.xml"));
			WeberknechtConf conf = WeberknechtConf.readConfig(in);
			
			{	//default area
				Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
				expectedActionClassMap.put("foo", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "pre1", "post1"));
				expectedActionClassMap.put("bar", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "", "post1"));
				assertEquals(expectedActionClassMap, conf.getActionClassMap());
			}
			
			{	//area a1
				Map<String, ActionDeclaration> expectedActionClassMap = new HashMap<String, ActionDeclaration>();
				expectedActionClassMap.put("foo1", new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "pre2", "post1"));
				expectedActionClassMap.put("bar1", new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "pre1", "post1"));
				assertEquals(expectedActionClassMap, conf.getActionClassMap("a1"));
			}			
			
			{
				Map<String, ProcessorList> expectedPre = new HashMap<String, ProcessorList>();
	
				//pre1
				List<String> expectedPreProcessorClasses = new Vector<String>();
				expectedPreProcessorClasses.add("de.highbyte_le.weberknecht.PreProcessor2");
				expectedPre.put("pre1", new ProcessorList("pre1", expectedPreProcessorClasses));
				
				//pre2
				expectedPreProcessorClasses = new Vector<String>();
				expectedPreProcessorClasses.add("de.highbyte_le.weberknecht.PreProcessor1");
				expectedPreProcessorClasses.add("de.highbyte_le.weberknecht.PreProcessor2");
				expectedPre.put("pre2", new ProcessorList("pre2", expectedPreProcessorClasses));
	
				assertEquals(expectedPre, conf.getPreProcessorListMap());
			}			
			
			{
				Map<String, ProcessorList> expectedPost = new HashMap<String, ProcessorList>();
	
				//post1
				List<String> expectedPostProcessorClasses = new Vector<String>();
				expectedPostProcessorClasses.add("de.highbyte_le.weberknecht.PostProcessor1");
				expectedPostProcessorClasses.add("de.highbyte_le.weberknecht.PostProcessor2");
				expectedPost.put("post1", new ProcessorList("post1", expectedPostProcessorClasses));
				
				assertEquals(expectedPost, conf.getPostProcessorListMap());
			}
			
			{	//referred processors
				ActionDeclaration declaration = conf.findActionDeclaration("", "foo");
				assertEquals("pre1", declaration.getPreProcessorSet());
				assertEquals("post1", declaration.getPostProcessorSet());
	
				declaration = conf.findActionDeclaration("", "bar");
				assertEquals("", declaration.getPreProcessorSet());
				assertEquals("post1", declaration.getPostProcessorSet());
	
				declaration = conf.findActionDeclaration("a1", "foo1");
				assertEquals("pre2", declaration.getPreProcessorSet());
				assertEquals("post1", declaration.getPostProcessorSet());
				
				declaration = conf.findActionDeclaration("a1", "bar1");
				assertEquals("pre1", declaration.getPreProcessorSet());
				assertEquals("post1", declaration.getPostProcessorSet());
				
				assertNull(conf.findActionDeclaration("a1", "foo"));
				assertNull(conf.findActionDeclaration("a2", "foo"));
			}
		}
		finally {
			if (in != null)
				in.close();
		}
	}
	
	/**
	 * Exception on old configuration
	 */
	@Test(expected=ConfigurationException.class)
	public void testReadConfigOld() throws IOException, ConfigurationException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File("test-data/weberknecht-old.xml"));
			WeberknechtConf.readConfig(in);
		}
		finally {
			if (in != null)
				in.close();
		}
	}
}
