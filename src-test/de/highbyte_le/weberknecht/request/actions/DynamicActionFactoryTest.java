/*
 * DynamicActionFactoryTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.highbyte_le.weberknecht.test.DummyAction1;
import de.highbyte_le.weberknecht.test.DummyAction2;

/**
 * @author pmairif
 */
public class DynamicActionFactoryTest {

	private DynamicActionFactory factory;
	
	@Before
	public void setUp() throws Exception {
		factory = new DynamicActionFactory();
		factory.registerAction("foo", "de.highbyte_le.weberknecht.test.DummyAction1");
		factory.registerAction("bar", "de.highbyte_le.weberknecht.test.DummyAction2");
	}

	/**
	 * Test method for {@link de.highbyte_le.weberknecht.request.actions.DynamicActionFactory#createAction(java.lang.String)}.
	 */
	@Test
	public void testCreateAction1() throws ActionInstantiationException {
		ExecutableAction action = factory.createAction("foo");
		assertNotNull(action);
		assertTrue(action instanceof DummyAction1);
	}

	@Test
	public void testCreateAction2() throws ActionInstantiationException {
		ExecutableAction action = factory.createAction("bar");
		assertNotNull(action);
		assertTrue(action instanceof DummyAction2);
	}

	@Test
	public void testCreateActionUnknown() throws ActionInstantiationException {
		ExecutableAction action = factory.createAction("bar-bar");
		assertNull(action);
	}

	/**
	 * foo is known, Foo not.
	 */
	@Test
	public void testCreateActionCase() throws ActionInstantiationException {
		ExecutableAction action = factory.createAction("Foo");
		assertNull(action);
	}
	
	/**
	 * Test method for {@link de.highbyte_le.weberknecht.request.actions.DynamicActionFactory#knowsAction(java.lang.String)}.
	 */
	@Test
	public void testKnowsAction() {
		assertTrue(factory.knowsAction("foo"));
		assertTrue(factory.knowsAction("bar"));
		
		assertFalse(factory.knowsAction("Foo"));
		assertFalse(factory.knowsAction("foo-bar"));
	}

	@Test(expected=ActionInstantiationException.class)
	public void testRegisterInvalidAction() throws ActionInstantiationException {
		factory.registerAction("abc", "com.example.Abc");
	}
}
