/*
 * RouteCollection.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing.flex;

import com.github.pmairif.weberknecht.request.routing.AreaPath;

import java.util.Arrays;
import java.util.List;

/**
 * holds routes
 */
public class RouteCollection {
    private final List<Route> routes;

    public RouteCollection(List<Route> routes) {
        this.routes = routes;
    }

    public RouteCollection(Route... routes) {
        this(Arrays.asList(routes));
    }

    public Route selectRoute(AreaPath path) {
        for (Route route: routes) {
            if (route.match(path))
                return route;
        }
        return null;
    }
}
