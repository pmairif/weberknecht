/*
 * ActionPathTest.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * testing ActionPath
 */
public class ActionPathTest {

    @Test
    public void testCreate() throws Exception {
        assertEquals("/foo", new ActionPath("/foo").getPath());
        assertEquals("/f/fooBar_1391", new ActionPath(" /f/fooBar_1391 ").getPath());
        assertEquals("/a", new ActionPath("/a").getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLeadingSlash() throws Exception {
        new ActionPath("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSlashAtTheEnd() throws Exception {
        new ActionPath("/foo/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSlashAndSpaceAtTheEnd() throws Exception {
        new ActionPath("/foo/ ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpaces() throws Exception {
        new ActionPath("/foo a");
    }

    @Test
    public void testCreateCompatible() throws Exception {
        ActionPath path = new ActionPath(new AreaPath("foo", "bar"), "default");
        assertEquals("/foo/bar/default", path.getPath());
    }

    @Test
    public void testPathActionSplitting1() throws Exception {
        ActionPath path = new ActionPath("/foo/list");
        assertEquals(new AreaPath("foo"), path.getAreaPath());
        assertEquals("list", path.getActionName());
    }

    @Test
    public void testPathActionSplitting2() throws Exception {
        ActionPath path = new ActionPath("/foo/bar/edit");
        assertEquals(new AreaPath("foo", "bar"), path.getAreaPath());
        assertEquals("edit", path.getActionName());
    }

    @Test
    public void testPathActionSplitting3() throws Exception {
        ActionPath path = new ActionPath("/foo");
        assertEquals(new AreaPath(), path.getAreaPath());
        assertEquals("foo", path.getActionName());
    }

    @Test
    public void testPathActionSplitting4() throws Exception {
        ActionPath path = new ActionPath("/foo.short/bar");
        assertEquals(new AreaPath("foo.short"), path.getAreaPath());
        assertEquals("bar", path.getActionName());
    }
}
