/*
 * DynamicTaskGenericRouteTest.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing.flex;

import com.github.pmairif.weberknecht.request.routing.ActionPath;
import com.github.pmairif.weberknecht.request.routing.AreaPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * testing DynamicTaskGenericRoute
 */
class DynamicTaskGenericRouteTest {

    @Test
    void testSimpleMatch() {
        DynamicTaskGenericRoute route = new DynamicTaskGenericRoute("foo", new ActionPath("/foo"));
        assertTrue(route.match(new AreaPath("foo")));
        assertFalse(route.match(new AreaPath("bar")));
        assertFalse(route.match(new AreaPath("foo", "bar")));
    }

    @Test
    void testMatchWithUnknownParameter() {
        DynamicTaskGenericRoute route = new DynamicTaskGenericRoute("foo/{di}", new ActionPath("/foo"));
        route.addParameterParser("id", new StringParameterParser());

        final AreaPath areaPath = new AreaPath("foo", "7627)");
        assertThrows(IllegalArgumentException.class, () -> route.match(areaPath));
    }

    @Test
    void testMatchWithParameterString() {
        DynamicTaskGenericRoute route = new DynamicTaskGenericRoute("foo/{id}", new ActionPath("/foo"));
        route.addParameterParser("id", new StringParameterParser());
        assertTrue(route.match(new AreaPath("foo", "7627")));
        assertTrue(route.match(new AreaPath("foo", "bar")));

        assertFalse(route.match(new AreaPath("foo")));
        assertFalse(route.match(new AreaPath("bar")));
    }

    @Test
    void testMatchWithMultipleParameters() {
        DynamicTaskGenericRoute route = new DynamicTaskGenericRoute("foo/{id}/{bar}", new ActionPath("/foo"));
        route.addParameterParser("id", new IntParameterParser());
        route.addParameterParser("bar", new StringParameterParser());
        assertTrue(route.match(new AreaPath("foo", "7628", "123")));
        assertEquals(7628, route.getParameterValues().get("id"));
        assertEquals("123", route.getParameterValues().get("bar"));

        assertFalse(route.match(new AreaPath("foo", "7628")));
    }

    @Test
    void testMatchWithTask() {
        DynamicTaskGenericRoute route = new DynamicTaskGenericRoute("foo/{id}/[task]", new ActionPath("/foo"));
        route.addParameterParser("id", new IntParameterParser());
        assertTrue(route.match(new AreaPath("foo", "7628", "store")));
        assertEquals(7628, route.getParameterValues().get("id"));
        assertEquals("store", route.getTask());

        assertTrue(route.match(new AreaPath("foo", "123", "delete")));
        assertEquals(123, route.getParameterValues().get("id"));
        assertEquals("delete", route.getTask());

        assertFalse(route.match(new AreaPath("foo", "7628")));
        assertFalse(route.match(new AreaPath("foo", "7628", "store$")));    //invalid task value
    }
}
