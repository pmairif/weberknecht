/*
 * VersionTest.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import de.highbyte_le.weberknecht.Version.VersionState;

/**
 * Testing {@link Version}
 *
 * @author pmairif
 */
public class VersionTest {

	@Before
	public void setUp() throws Exception {
	}

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
		
		assertFalse(actual.hasBuildNumber());
		assertFalse(actual.hasBranch());
		assertFalse(actual.hasVersionState());
		
		assertEquals(0, actual.getBuildNumber());
		assertNull(actual.getBranch());
		assertNull(actual.getVersionState());
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
		props.setProperty("version.weberknecht.build", "99");
		props.setProperty("version.weberknecht.branch", "foo");
		props.setProperty("version.weberknecht.state", "beta");
		
		Version actual = Version.parseProperties(props);
		assertEquals(1, actual.getMajor());
		assertEquals(2, actual.getMinor());
		assertEquals(3, actual.getPatch());
		
		assertTrue(actual.hasBuildNumber());
		assertTrue(actual.hasBranch());
		assertTrue(actual.hasVersionState());
		
		assertEquals(99, actual.getBuildNumber());
		assertEquals(VersionState.BETA, actual.getVersionState());
		assertEquals("foo", actual.getBranch());
	}
	
	/**
	 * Test method for {@link de.highbyte_le.weberknecht.Version#getVersionString(de.highbyte_le.weberknecht.Version.VersionFormat)}.
	 */
	@Test
	public void testGetVersionString() {
		fail("Not yet implemented");
	}

}
