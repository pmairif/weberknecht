/*
 * LocalePathResolverTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import com.github.pmairif.weberknecht.conf.WeberknechtConf;
import com.github.pmairif.weberknecht.request.actions.ActionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.github.pmairif.weberknecht.test.TestUtil.readConfig;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author pmairif
 */
public class LocalePathResolverTest {

	private LocalePathResolver resolver;
	
	@BeforeEach
	public void setUp() throws Exception {
		WeberknechtConf conf = readConfig("test-data/weberknecht-lang-optional.xml");
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

    @Test
    public void testCreatePathWithoutLocale() throws Exception {
        LocalePath expected = new LocalePath(new String[]{"foo", "bar"}, null);
        assertEquals(expected, resolver.createPath("/foo/bar"));
        assertEquals(expected, resolver.createPath("foo/bar"));
        assertEquals(expected, resolver.createPath("foo/bar/"));
        assertEquals(expected, resolver.createPath("/foo/bar/"));
    }

    @Test
    public void testCreatePathWithLocale() throws Exception {
        LocalePath expected = new LocalePath(new String[]{"foo", "bar"}, new Locale("de"));
        assertEquals(expected, resolver.createPath("/de/foo/bar"));
        assertEquals(expected, resolver.createPath("de/foo/bar"));
    }

    @Test
    public void testObligatoryLanguage() throws Exception {
        WeberknechtConf conf = readConfig("test-data/weberknecht-lang.xml");
        LocalePathResolver resolver = new LocalePathResolver(conf);

		assertThrows(RoutingNotPossibleException.class, () -> resolver.createPath("/foo/bar"));
    }
}
