/*
 * DynamicActionFactoryTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.actions;

import com.github.pmairif.weberknecht.test.DummyAction1;
import com.github.pmairif.weberknecht.test.DummyAction2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author pmairif
 */
class DynamicActionFactoryTest {

	private DynamicActionFactory factory;
	
	@BeforeEach
	public void setUp() throws ActionInstantiationException {
		factory = new DynamicActionFactory();
		factory.registerAction("foo", "com.github.pmairif.weberknecht.test.DummyAction1");
		factory.registerAction("bar", "com.github.pmairif.weberknecht.test.DummyAction2");
	}

	/**
	 * Test method for {@link DynamicActionFactory#createAction(java.lang.String)}.
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
	 * Test method for {@link DynamicActionFactory#knowsAction(java.lang.String)}.
	 */
	@Test
	public void testKnowsAction() {
		assertTrue(factory.knowsAction("foo"));
		assertTrue(factory.knowsAction("bar"));
		
		assertFalse(factory.knowsAction("Foo"));
		assertFalse(factory.knowsAction("foo-bar"));
	}

	@Test
	void testRegisterInvalidAction() {
		assertThrows(ActionInstantiationException.class, () -> factory.registerAction("abc", "com.example.Abc"));
	}
}
