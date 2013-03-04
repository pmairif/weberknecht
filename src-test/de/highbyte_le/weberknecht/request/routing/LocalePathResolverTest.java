/*
 * LocalePathResolverTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import static de.highbyte_le.weberknecht.test.TestUtil.readConfig;
import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import de.highbyte_le.weberknecht.conf.WeberknechtConf;

/**
 * @author pmairif
 */
public class LocalePathResolverTest {

	private LocalePathResolver resolver;
	
	@Before
	public void setUp() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
		resolver = new LocalePathResolver(conf);
	}

	@Test
	public void testParseLocale() throws Exception {
		assertEquals(new Locale("de"), resolver.parseLocale("de"));
		assertEquals(new Locale("de"), resolver.parseLocale(" de "));
		assertEquals(new Locale("en"), resolver.parseLocale("en"));
		assertEquals(new Locale("en", "US"), resolver.parseLocale("en_US"));
		
		assertNull(resolver.parseLocale(""));
		assertNull(resolver.parseLocale("  "));
		assertNull(resolver.parseLocale(null));
	}
}
