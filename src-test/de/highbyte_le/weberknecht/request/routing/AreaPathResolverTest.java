/*
 * AreaPathResolverTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
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

import org.junit.Test;

import de.highbyte_le.weberknecht.conf.ActionDeclaration;
import de.highbyte_le.weberknecht.conf.ConfigurationException;
import de.highbyte_le.weberknecht.conf.WeberknechtConf;
import de.highbyte_le.weberknecht.request.actions.ActionInstantiationException;
import de.highbyte_le.weberknecht.request.actions.ActionNotFoundException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;

/**
 * Testing {@link AreaPathResolver}
 *
 * @author pmairif
 */
public class AreaPathResolverTest {

	@Test
	public void testResolveAction() throws IOException, ConfigurationException, ActionNotFoundException, ActionInstantiationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		ExecutableAction actual = resolver.resolveAction(new RoutingTarget(new AreaPath(), "foo", "do", null));
		assertEquals("de.highbyte_le.weberknecht.test.DummyAction1", actual.getClass().getCanonicalName());

		actual = resolver.resolveAction(new RoutingTarget(new AreaPath(), "bar", "do", null));
		assertEquals("de.highbyte_le.weberknecht.test.DummyAction2", actual.getClass().getCanonicalName());

		actual = resolver.resolveAction(new RoutingTarget(new AreaPath("sub"), "foo1", "do", null));
		assertEquals("de.highbyte_le.weberknecht.test.DummyAction3", actual.getClass().getCanonicalName());
	}

	@Test(expected=ActionNotFoundException.class)
	public void testResolveActionNoRoutingTarget() throws IOException, ConfigurationException, ActionNotFoundException, ActionInstantiationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		resolver.resolveAction(null);
	}
	
	@Test(expected=ActionNotFoundException.class)
	public void testResolveActionNoActionName() throws IOException, ConfigurationException, ActionNotFoundException, ActionInstantiationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		resolver.resolveAction(new RoutingTarget(null, "do", null));
	}
	
	@Test(expected=ActionNotFoundException.class)
	public void testResolveActionInvalidActionName() throws IOException, ConfigurationException, ActionNotFoundException, ActionInstantiationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		resolver.resolveAction(new RoutingTarget(new AreaPath(), "xyz", "do", null));
	}
	
	@Test(expected=ActionNotFoundException.class)
	public void testResolveActionInvalidPath() throws IOException, ConfigurationException, ActionNotFoundException, ActionInstantiationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		resolver.resolveAction(new RoutingTarget(new AreaPath("x"), "foo", "do", null));
	}
	
	@Test
	public void testGetActionDeclaration1() throws IOException, ConfigurationException {
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
	
	@Test
	public void testGetActionDeclaration2() throws IOException, ConfigurationException {
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
	public void testGetActionDeclaration3() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-6a.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		ActionDeclaration expected = new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "", "", "de.highbyte_le.weberknecht.ErrHandler1");
		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "", "", "de.highbyte_le.weberknecht.ErrHandler3");
		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "", "", "de.highbyte_le.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "", "", "de.highbyte_le.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals(expected, declaration);

		expected = new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "", "", "de.highbyte_le.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "foo2");
		assertEquals(expected, declaration);

		expected = new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "", "", "de.highbyte_le.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "bar2");
		assertEquals(expected, declaration);
	}
	
	@Test
	public void testGetActionDeclaration4() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-6b.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		ActionDeclaration expected = new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "", "", "de.highbyte_le.weberknecht.ErrHandler1");
		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals(expected, declaration);

		expected = new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "", "", "de.highbyte_le.weberknecht.ErrHandler3");
		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "", "", "de.highbyte_le.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "", "", "de.highbyte_le.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "", "", "de.highbyte_le.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "foo2");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.FooAction", "", "", "de.highbyte_le.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2", "a3"), "foo3");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.BarAction", "", "", "de.highbyte_le.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "bar2");
		assertEquals(expected, declaration);
	}

	@Test
	public void testGetActionDeclaration5() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		ActionDeclaration expected = new ActionDeclaration("de.highbyte_le.weberknecht.test.DummyAction1", "pre1", "post1", null);
		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.test.DummyAction2", "pre1", "post1", null);
		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("de.highbyte_le.weberknecht.test.DummyAction3", "pre2", "post1", null);
		declaration = resolver.getActionDeclaration(new AreaPath("sub"), "foo1");
		assertEquals(expected, declaration);
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
