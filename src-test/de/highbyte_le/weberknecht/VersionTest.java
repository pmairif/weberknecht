/*
 * VersionTest.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

import de.highbyte_le.weberknecht.Version.VersionFormat;

/**
 * Testing {@link Version}
 *
 * @author pmairif
 */
public class VersionTest {

	/**
	 * Simple test set, just major, minor and patch
	 */
	@Test
	public void testParsePropertiesSimple() {
		Properties props = new Properties();
		props.setProperty("version.weberknecht.major", "1");
		props.setProperty("version.weberknecht.minor", "2");
		props.setProperty("version.weberknecht.patch", "3");
		
		Version actual = Version.parseProperties(props);
		assertEquals(1, actual.getMajor());
		assertEquals(2, actual.getMinor());
		assertEquals(3, actual.getPatch());
		
		assertFalse(actual.hasBranch());
		
		assertNull(actual.getBranch());
	}

	/**
	 * Full test set, all properties
	 */
	@Test
	public void testParsePropertiesFull() {
		Properties props = new Properties();
		props.setProperty("version.weberknecht.major", "1");
		props.setProperty("version.weberknecht.minor", "2");
		props.setProperty("version.weberknecht.patch", "3");
		props.setProperty("version.weberknecht.branch", "foo");
		
		Version actual = Version.parseProperties(props);
		assertEquals(1, actual.getMajor());
		assertEquals(2, actual.getMinor());
		assertEquals(3, actual.getPatch());
		
		assertTrue(actual.hasBranch());
		
		assertEquals("foo", actual.getBranch());
	}
	
	/**
	 * Test method for {@link de.highbyte_le.weberknecht.Version#getVersionString(de.highbyte_le.weberknecht.Version.VersionFormat)}.
	 */
	@Test
	public void testGetVersionString() {
		Version version = new Version(2, 1, 21, "develop");
		assertEquals("2.1", version.getVersionString(VersionFormat.SHORT));
		assertEquals("2.1.21-develop", version.getVersionString(VersionFormat.FULL));
	}
}
