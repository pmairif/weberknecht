/*
 * RpxTaglibBaseTest.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.rpx.taglibs;

import static org.junit.Assert.*;

import javax.servlet.jsp.JspException;

import org.junit.Before;
import org.junit.Test;

public class RpxTaglibBaseTest {
	
	private RpxTaglibBase base;

	@SuppressWarnings("serial")
	@Before
	public void setUp() throws Exception {
		base = new RpxTaglibBase() {};
	}

	@Test
	public void testGetTokenUrl1() throws JspException {
		base.setResponsePath("/foo");
		assertEquals("http://example.com/foo?do=rpx", base.getTokenUrl("http://example.com"));
	}

	@Test
	public void testGetTokenUrl2() throws JspException {
		base.setResponsePath("foo/bar");
		assertEquals("http://example.com/foo/bar?do=rpx", base.getTokenUrl("http://example.com"));
	}
	
	@Test
	public void testGetTokenUrl3() throws JspException {
		base.setResponsePath("/foo");
		assertEquals("http://example.com/foo?do=rpx", base.getTokenUrl("http://example.com/"));
	}
	
	@Test
	public void testGetTokenUrl4() throws JspException {
		base.setResponsePath("foo/bar");
		assertEquals("http://example.com/foo/bar?do=rpx", base.getTokenUrl("http://example.com/"));
	}
	
	@Test
	public void testGetTokenUrl5() throws JspException {
		base.setResponsePath("foo?bar");
		assertEquals("http://example.com/foo?bar&do=rpx", base.getTokenUrl("http://example.com/"));
	}
}
