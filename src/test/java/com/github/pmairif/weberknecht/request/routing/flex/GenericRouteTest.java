/*
 * GenericRouteTest.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing.flex;

import com.github.pmairif.weberknecht.request.routing.ActionPath;
import com.github.pmairif.weberknecht.request.routing.AreaPath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * testing GenericRoute
 */
public class GenericRouteTest {

    @Test
    public void testSimpleMatch() throws Exception {
        GenericRoute route = new GenericRoute("foo", new ActionPath("/foo"));
        assertTrue(route.match(new AreaPath("foo")));
        assertFalse(route.match(new AreaPath("bar")));
        assertFalse(route.match(new AreaPath("foo", "bar")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchWithUnknownParameter() throws Exception {
        GenericRoute route = new GenericRoute("foo/{di}", new ActionPath("/foo"));
        route.addParameterParser("id", new StringParameterParser());
        route.match(new AreaPath("foo", "7627)"));
    }

    @Test
    public void testMatchWithParameterString() throws Exception {
        GenericRoute route = new GenericRoute("foo/{id}", new ActionPath("/foo"));
        route.addParameterParser("id", new StringParameterParser());
        assertTrue(route.match(new AreaPath("foo", "7627")));
        assertTrue(route.match(new AreaPath("foo", "bar")));

        assertFalse(route.match(new AreaPath("foo")));
        assertFalse(route.match(new AreaPath("bar")));
    }

    @Test
    public void testMatchWithParameterInt() throws Exception {
        GenericRoute route = new GenericRoute("foo/{id}", new ActionPath("/foo"));
        route.addParameterParser("id", new IntParameterParser());
        assertTrue(route.match(new AreaPath("foo", "7627")));
        assertEquals(7627, route.getParameterValues().get("id"));

        assertFalse(route.match(new AreaPath("foo", "bar")));
        assertFalse(route.match(new AreaPath("foo")));
        assertFalse(route.match(new AreaPath("bar")));
    }

    @Test
    public void testMatchWithParameterId() throws Exception {
        GenericRoute route = new GenericRoute("foo/{id}", new ActionPath("/foo"));
        route.addParameterParser("id", DummyId.getParameterParser());
        assertTrue(route.match(new AreaPath("foo", "7627")));

        assertFalse(route.match(new AreaPath("foo", "bar")));
        assertFalse(route.match(new AreaPath("foo")));
        assertFalse(route.match(new AreaPath("bar")));
    }

    @Test
    public void testMatchWithMultipleParameters() throws Exception {
        GenericRoute route = new GenericRoute("foo/{id}/{bar}", new ActionPath("/foo"));
        route.addParameterParser("id", new IntParameterParser());
        route.addParameterParser("bar", new StringParameterParser());
        assertTrue(route.match(new AreaPath("foo", "7628", "123")));
        assertEquals(7628, route.getParameterValues().get("id"));
        assertEquals("123", route.getParameterValues().get("bar"));

        assertFalse(route.match(new AreaPath("foo", "7628")));
    }
}
