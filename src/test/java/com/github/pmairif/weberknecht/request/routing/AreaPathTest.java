/*
 * AreaPathTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 20.01.2013
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * testing AreaPath
 *
 * @author rick
 */
public class AreaPathTest {

	@Test
	public void testEmptyPathIgnored1() {
		assertEquals(new AreaPath(), new AreaPath(""));
	}

	@Test
	public void testEmptyPathIgnored2() {
		AreaPath path = new AreaPath();
		
		path.addPath("");
		assertEquals(0, path.getAreas().size());

		path.addPath(null);
		assertEquals(0, path.getAreas().size());
	}
	
	@Test
	public void testAddPath() {
		AreaPath path = new AreaPath();

		path.addPath("foo").addPath("bar");
		
		List<String> expected = new Vector<String>();
		expected.add("foo");
		expected.add("bar");
		assertEquals(expected, path.getAreas());
	}

	@Test
	public void testConstructor() {
		AreaPath path = new AreaPath("a1", "a2");
		
		List<String> expected = new Vector<String>();
		expected.add("a1");
		expected.add("a2");
		assertEquals(expected, path.getAreas());
	}
	
	@Test
	public void testClone() {
		AreaPath path1 = new AreaPath("a1");
		AreaPath path2 = path1.clone();
		
		path1.addPath("a2");
		assertEquals(2, path1.getAreas().size());
		assertEquals(1, path2.getAreas().size());
	}

	@Test
	public void testFork() {
		AreaPath path1 = new AreaPath("a1");
		AreaPath path2 = path1.fork("a2");
		
		assertEquals(1, path1.getAreas().size());
		assertEquals(2, path2.getAreas().size());
	}
}
