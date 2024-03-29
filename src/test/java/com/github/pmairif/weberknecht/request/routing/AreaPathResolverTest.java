/*
 * AreaPathResolverTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import com.github.pmairif.weberknecht.conf.ActionDeclaration;
import com.github.pmairif.weberknecht.conf.ConfigurationException;
import com.github.pmairif.weberknecht.conf.WeberknechtConf;
import com.github.pmairif.weberknecht.request.actions.ActionInstantiationException;
import com.github.pmairif.weberknecht.request.actions.ActionNotFoundException;
import com.github.pmairif.weberknecht.request.actions.ExecutableAction;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.pmairif.weberknecht.test.TestUtil.readConfig;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing {@link AreaPathResolver}
 *
 * @author pmairif
 */
class AreaPathResolverTest {

	@Test
	public void testResolveAction() throws IOException, ConfigurationException, ActionNotFoundException, ActionInstantiationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		ExecutableAction actual = resolver.resolveAction(new RoutingTarget(new AreaPath(), "foo", "do", null));
		assertEquals("com.github.pmairif.weberknecht.test.DummyAction1", actual.getClass().getCanonicalName());

		actual = resolver.resolveAction(new RoutingTarget(new AreaPath(), "bar", "do", null));
		assertEquals("com.github.pmairif.weberknecht.test.DummyAction2", actual.getClass().getCanonicalName());

		actual = resolver.resolveAction(new RoutingTarget(new AreaPath("sub"), "foo1", "do", null));
		assertEquals("com.github.pmairif.weberknecht.test.DummyAction3", actual.getClass().getCanonicalName());
	}

	@Test
	public void testKnownAction() throws IOException, ConfigurationException, ActionNotFoundException, ActionInstantiationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		assertTrue(resolver.knownTarget(new RoutingTarget(new AreaPath(), "foo", "do", null)));
		assertTrue(resolver.knownTarget(new RoutingTarget(new AreaPath(), "bar", "do", null)));
		assertTrue(resolver.knownTarget(new RoutingTarget(new AreaPath("sub"), "foo1", "do", null)));

		assertFalse(resolver.knownTarget(new RoutingTarget(new AreaPath(), "fuu", "do", null)));
		assertFalse(resolver.knownTarget(new RoutingTarget(new AreaPath(), null, "do", null)));
		assertFalse(resolver.knownTarget(null));
	}
	
	@Test
	public void testResolveActionNoRoutingTarget() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		assertThrows(ActionNotFoundException.class, () -> resolver.resolveAction(null));
	}
	
	@Test
	public void testResolveActionNoActionName() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		final RoutingTarget routingTarget = new RoutingTarget(null, "do", null);
		assertThrows(ActionNotFoundException.class, () -> resolver.resolveAction(routingTarget));
	}
	
	@Test
	public void testResolveActionInvalidActionName() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		final RoutingTarget routingTarget = new RoutingTarget(new AreaPath(), "xyz", "do", null);
		assertThrows(ActionNotFoundException.class, () -> resolver.resolveAction(routingTarget));
	}
	
	@Test
	public void testResolveActionInvalidPath() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		final RoutingTarget routingTarget = new RoutingTarget(new AreaPath("x"), "foo", "do", null);
		assertThrows(ActionNotFoundException.class, () -> resolver.resolveAction(routingTarget));
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
		assertEquals("com.github.pmairif.weberknecht.ErrHandler1", declaration.getErrorHandlerClass());

		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals("com.github.pmairif.weberknecht.ErrHandler3", declaration.getErrorHandlerClass());

		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals("com.github.pmairif.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
		
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals("com.github.pmairif.weberknecht.ErrHandler2", declaration.getErrorHandlerClass());
	}
	
	
	@Test
	public void testGetActionDeclaration3() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-6a.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		ActionDeclaration expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler1");
		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler3");
		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals(expected, declaration);

		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "foo2");
		assertEquals(expected, declaration);

		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "bar2");
		assertEquals(expected, declaration);
	}
	
	@Test
	public void testGetActionDeclaration4() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-6b.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);

		ActionDeclaration expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler1");
		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals(expected, declaration);

		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler3");
		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "foo1");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1"), "bar1");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "foo2");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "", "", "com.github.pmairif.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2", "a3"), "foo3");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "", "", "com.github.pmairif.weberknecht.ErrHandler2");
		declaration = resolver.getActionDeclaration(new AreaPath("a1", "a2"), "bar2");
		assertEquals(expected, declaration);
	}

	@Test
	public void testGetActionDeclaration5() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-8.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		ActionDeclaration expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction1", "pre1", "post1", null);
		ActionDeclaration declaration = resolver.getActionDeclaration(new AreaPath(), "foo");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction2", "pre1", "post1", null);
		declaration = resolver.getActionDeclaration(new AreaPath(), "bar");
		assertEquals(expected, declaration);
		
		expected = new ActionDeclaration("com.github.pmairif.weberknecht.test.DummyAction3", "pre2", "post1", null);
		declaration = resolver.getActionDeclaration(new AreaPath("sub"), "foo1");
		assertEquals(expected, declaration);
	}
	
	@Test
	public void testGetActionDeclarationNull() throws IOException, ConfigurationException {
		WeberknechtConf conf = readConfig("test-data/weberknecht-1.xml");
		AreaPathResolver resolver = new AreaPathResolver(conf);
		
		assertNull(resolver.getActionDeclaration(null));
	}
}
