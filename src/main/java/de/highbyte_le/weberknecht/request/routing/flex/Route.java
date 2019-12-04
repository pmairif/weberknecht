/*
 * Route.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing.flex;

import de.highbyte_le.weberknecht.request.routing.ActionPath;
import de.highbyte_le.weberknecht.request.routing.AreaPath;

import java.util.Map;

/**
 * Handles routing of a single URL pattern. Used by FlexRouter.
 */
public interface Route {
    boolean match(AreaPath path);

    Map<String, Object> getParameterValues();

    /**
     * target action path
     */
    ActionPath getActionPath();

    /**
     * target action processor
     */
    String getProcessor();

    /**
     * target action task
     */
    String getTask();
}
