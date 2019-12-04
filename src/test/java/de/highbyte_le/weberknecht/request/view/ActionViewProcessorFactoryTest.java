/*
 * ActionViewProcessorFactoryTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.view;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

/**
 * @author pmairif
 */
public class ActionViewProcessorFactoryTest {

	private ActionViewProcessorFactory factory;
	
	private ServletContext servletContext;
	
	@Before
	public void setUp() throws Exception {
		factory = new ActionViewProcessorFactory();
		
		servletContext = mock(ServletContext.class);
	}

	/**
	 * default suffix do
	 */
	@Test
	public void testCreateActionProcessorDefaultSuffixDo() {
		ActionViewProcessor actual = factory.createActionProcessor("do", servletContext);
		assertNotNull(actual);
		assertTrue(actual instanceof WebActionProcessor);
	}

	/**
	 * default suffix ""
	 */
	@Test
	public void testCreateActionProcessorDefaultNoSuffix() {
		ActionViewProcessor actual = factory.createActionProcessor("", servletContext);
		assertNotNull(actual);
		assertTrue(actual instanceof WebActionProcessor);
	}
	
	/**
	 * default suffix feed
	 */
	@Test
	public void testCreateActionProcessorDefaultSuffixFeed() {
		ActionViewProcessor actual = factory.createActionProcessor("feed", servletContext);
		assertNotNull(actual);
		assertTrue(actual instanceof FeedActionProcessor);
	}
	
	/**
	 * default suffix data
	 */
	@Test
	public void testCreateActionProcessorDefaultSuffixData() {
		ActionViewProcessor actual = factory.createActionProcessor("data", servletContext);
		assertNotNull(actual);
		assertTrue(actual instanceof DataActionProcessor);
	}
	
	/**
	 * default suffix json
	 */
	@Test
	public void testCreateActionProcessorDefaultSuffixJson() {
		ActionViewProcessor actual = factory.createActionProcessor("json", servletContext);
		assertNotNull(actual);
		assertTrue(actual instanceof JsonActionProcessor);
	}
	
	/**
	 * unknown suffix
	 */
	@Test
	public void testCreateActionProcessorUnknownSuffix() {
		ActionViewProcessor actual = factory.createActionProcessor("foo", servletContext);
		assertNull(actual);
	}
	
	
	/**
	 * register new suffix
	 */
	@Test
	public void testRegisterProcessorStringString() throws ClassNotFoundException {
		assertNull(factory.createActionProcessor("foo", servletContext));
		assertNull(factory.createActionProcessor("bar", servletContext));
		
		factory.registerProcessor("foo", "de.highbyte_le.weberknecht.request.view.DataActionProcessor");
		factory.registerProcessor("bar", "de.highbyte_le.weberknecht.request.view.WebActionProcessor");
		
		ActionViewProcessor actual = factory.createActionProcessor("foo", servletContext);
		assertNotNull(actual);
		assertTrue(actual instanceof DataActionProcessor);
		actual = factory.createActionProcessor("bar", servletContext);
		assertNotNull(actual);
		assertTrue(actual instanceof WebActionProcessor);
	}

}
