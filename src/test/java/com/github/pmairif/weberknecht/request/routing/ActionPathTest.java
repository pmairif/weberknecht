/*
 * ActionPathTest.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * testing ActionPath
 */
class ActionPathTest {

	@Test
	void testCreate() {
		assertEquals("/foo", new ActionPath("/foo").getPath());
		assertEquals("/f/fooBar_1391", new ActionPath(" /f/fooBar_1391 ").getPath());
		assertEquals("/a", new ActionPath("/a").getPath());
	}

	@Test
	void testMissingLeadingSlash() {
		assertThrows(IllegalArgumentException.class, () -> new ActionPath("foo"));
	}

	@Test
	void testSlashAtTheEnd() {
		assertThrows(IllegalArgumentException.class, () -> new ActionPath("/foo/"));
	}

	@Test
	void testSlashAndSpaceAtTheEnd() {
		assertThrows(IllegalArgumentException.class, () -> new ActionPath("/foo/ "));
	}

	@Test
	void testSpaces() {
		assertThrows(IllegalArgumentException.class, () -> new ActionPath("/foo a"));
	}

	@Test
	void testCreateCompatible() {
		ActionPath path = new ActionPath(new AreaPath("foo", "bar"), "default");
		assertEquals("/foo/bar/default", path.getPath());
	}

	@Test
	void testPathActionSplitting1() {
		ActionPath path = new ActionPath("/foo/list");
		assertEquals(new AreaPath("foo"), path.getAreaPath());
		assertEquals("list", path.getActionName());
	}

	@Test
	void testPathActionSplitting2() {
		ActionPath path = new ActionPath("/foo/bar/edit");
		assertEquals(new AreaPath("foo", "bar"), path.getAreaPath());
		assertEquals("edit", path.getActionName());
	}

	@Test
	void testPathActionSplitting3() {
		ActionPath path = new ActionPath("/foo");
		assertEquals(new AreaPath(), path.getAreaPath());
		assertEquals("foo", path.getActionName());
	}

	@Test
	void testPathActionSplitting4() {
		ActionPath path = new ActionPath("/foo.short/bar");
		assertEquals(new AreaPath("foo.short"), path.getAreaPath());
		assertEquals("bar", path.getActionName());
	}
}
