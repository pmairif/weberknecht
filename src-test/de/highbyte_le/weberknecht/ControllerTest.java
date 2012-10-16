/*
 * ControllerTest.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 16.10.2012
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.request.processing.ActionExecution;
import de.highbyte_le.weberknecht.request.processing.Processor;
import de.highbyte_le.weberknecht.test.DummyProcessor1;
import de.highbyte_le.weberknecht.test.DummyProcessor2;

/**
 * Testing Controller
 *
 * @author rick
 */
public class ControllerTest {
	
	private Controller controller;
	
	@Before
	public void setUp() throws Exception {
		this.controller = new Controller();
		
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File("test-data/weberknecht-4.xml"));
			WeberknechtConf conf = WeberknechtConf.readConfig(in);
			controller.setConf(conf);
		}
		finally {
			if (in != null)
				in.close();
		}
	}

	@Test
	public void testSetupProcessorsDefaultFoo() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Processor> processors = controller.setupProcessors("", "foo");
		
		//pre1 and post1 expected
		int i = 0;
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//pre1
		assertTrue(processors.get(i++) instanceof ActionExecution);	//action
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//post1
		assertTrue(processors.get(i++) instanceof DummyProcessor2);
	}
	
	@Test
	public void testSetupProcessorsDefaultBar() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Processor> processors = controller.setupProcessors("", "bar");
		
		//post1 expected
		int i = 0;
		assertTrue(processors.get(i++) instanceof ActionExecution);	//action
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//post1
		assertTrue(processors.get(i++) instanceof DummyProcessor2);
	}

	@Test
	public void testSetupProcessorsA1Foo1() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Processor> processors = controller.setupProcessors("a1", "foo1");
		
		//pre2 and post1 expected
		int i = 0;
		assertTrue(processors.get(i++) instanceof DummyProcessor2);	//pre2
		assertTrue(processors.get(i++) instanceof ActionExecution);	//action
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//post1
		assertTrue(processors.get(i++) instanceof DummyProcessor2);
	}
	
	@Test
	public void testSetupProcessorsA1Bar1() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Processor> processors = controller.setupProcessors("a1", "bar1");
		
		//pre1 and post1 expected
		int i = 0;
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//pre1
		assertTrue(processors.get(i++) instanceof ActionExecution);	//action
		assertTrue(processors.get(i++) instanceof DummyProcessor1);	//post1
		assertTrue(processors.get(i++) instanceof DummyProcessor2);
	}
}
