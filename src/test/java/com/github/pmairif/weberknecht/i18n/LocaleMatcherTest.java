/*
 * LocaleMatcherTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.i18n;


import com.github.pmairif.weberknecht.request.actions.ActionExecutionException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * testing {@link LocaleMatcher}
 *
 * @author pmairif
 */
public class LocaleMatcherTest {

	private static final String BUNDLE = "com.github.pmairif.weberknecht.test.messages";
	
	private LocaleMatcher matcher;
	
	/**
	 * empty locales, expect default
	 */
	@Test
	public void testGetLocaleEmptyDefaultDe() {
		List<Locale> locales = new Vector<Locale>();
		matcher = new LocaleMatcher(BUNDLE, locales);
		matcher.setDefaultLocale(new Locale("de"));
		
		assertEquals("de", matcher.findLocale().toString());
	}
	
	/**
	 * empty locales, expect default
	 */
	@Test
	public void testGetLocaleEmptyDefaultEn() {
		List<Locale> locales = new Vector<Locale>();
		matcher = new LocaleMatcher(BUNDLE, locales);
		matcher.setDefaultLocale(new Locale("en"));
		
		assertEquals("en", matcher.findLocale().toString());
	}
	
	/**
	 * empty locales, default not present, expect ""
	 */
	@Test
	public void testGetLocaleEmptyDefaultFr() {
		List<Locale> locales = new Vector<Locale>();
		matcher = new LocaleMatcher(BUNDLE, locales);
		matcher.setDefaultLocale(new Locale("fr"));
		
		assertEquals("Foo", matcher.findResourceBundle().getString("test.foo"));
		assertEquals("", matcher.findLocale().toString());
	}
	
	/**
	 * de_DE, en_US, expect de
	 */
	@Test
	public void testGetLocaleDe() throws ActionExecutionException {
		List<Locale> locales = new Vector<Locale>();
		locales.add(new Locale("de", "DE"));
		locales.add(new Locale("en", "US"));
		
		matcher = new LocaleMatcher(BUNDLE, locales);
		matcher.setDefaultLocale(new Locale("en"));
		
		assertEquals("FooDe", matcher.findResourceBundle().getString("test.foo"));
		assertEquals("de", matcher.findLocale().toString());
	}

	/**
	 * de_CH, de, expect de_CH
	 */
	@Test
	public void testGetLocaleDeCh() throws ActionExecutionException {
		List<Locale> locales = new Vector<Locale>();
		locales.add(new Locale("de", "CH"));
		locales.add(new Locale("de"));
		
		matcher = new LocaleMatcher(BUNDLE, locales);
		matcher.setDefaultLocale(new Locale("en"));
		
		assertEquals("FooCh", matcher.findResourceBundle().getString("test.foo"));
		assertEquals("de_CH", matcher.findLocale().toString());
	}
	
	/**
	 * de, de_CH, expect de
	 */
	@Test
	public void testGetLocaleDeCh2() throws ActionExecutionException {
		List<Locale> locales = new Vector<Locale>();
		locales.add(new Locale("de"));
		locales.add(new Locale("de", "CH"));
		
		matcher = new LocaleMatcher(BUNDLE, locales);
		matcher.setDefaultLocale(new Locale("en"));
		
		assertEquals("FooDe", matcher.findResourceBundle().getString("test.foo"));
		assertEquals("de", matcher.findLocale().toString());
	}
	
	/**
	 * en_US, expect en
	 */
	@Test
	public void testGetStringEn() throws ActionExecutionException {
		List<Locale> locales = new Vector<Locale>();
		locales.add(new Locale("en", "US"));

		matcher = new LocaleMatcher(BUNDLE, locales);
		matcher.setDefaultLocale(new Locale("en"));
		
		assertEquals("FooEn", matcher.findResourceBundle().getString("test.foo"));
		assertEquals("en", matcher.findLocale().toString());
	}
	
	/**
	 * requested es, expected en
	 */
	@Test
	public void testGetStringEs() throws ActionExecutionException {
		List<Locale> locales = new Vector<Locale>();
		locales.add(new Locale("es"));

		matcher = new LocaleMatcher(BUNDLE, locales);
		matcher.setDefaultLocale(new Locale("en"));
		
		assertEquals("en", matcher.findLocale().toString());
	}
	
	/**
	 * requested es or de, expected de
	 */
	@Test
	public void testGetStringEsDe() throws ActionExecutionException {
		List<Locale> locales = new Vector<Locale>();
		locales.add(new Locale("es"));
		locales.add(new Locale("de"));

		matcher = new LocaleMatcher(BUNDLE, locales);
		matcher.setDefaultLocale(new Locale("en"));
		
		assertEquals("FooDe", matcher.findResourceBundle().getString("test.foo"));
		assertEquals("de", matcher.findLocale().toString());
	}
}
