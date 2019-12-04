/*
 * RouteCollectionTest.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing.flex;

import de.highbyte_le.weberknecht.request.routing.ActionPath;
import de.highbyte_le.weberknecht.request.routing.AreaPath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * testing RouteCollection
 */
public class RouteCollectionTest {
    @Test
    public void testSelectSimpleRoute() throws Exception {
        GenericRoute routeFoo = new GenericRoute("foo", new ActionPath("/foo"));
        GenericRoute routeBar = new GenericRoute("foo/bar", new ActionPath("/foo/bar"));

        RouteCollection selector = new RouteCollection(routeFoo, routeBar);

        assertEquals(routeFoo, selector.selectRoute(new AreaPath("foo")));
        assertEquals(routeBar, selector.selectRoute(new AreaPath("foo", "bar")));
        assertNull(selector.selectRoute(new AreaPath("foobar")));
        assertNull(selector.selectRoute(new AreaPath("foo", "bar", "fd")));
        assertNull(selector.selectRoute(new AreaPath("fd", "foo", "bar")));
    }
}
