/*
 * DynamicTaskGenericRoute.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing.flex;

import de.highbyte_le.weberknecht.request.routing.ActionPath;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * routing definition entity with dynamic tasks
 */
public class DynamicTaskGenericRoute extends GenericRoute {

    private static final Pattern validTaskPattern = Pattern.compile("[a-zA-Z0-9_-]+");

    /**
     * optional target action task
     */
    private String task = null;

    public DynamicTaskGenericRoute(String pattern, ActionPath actionPath) {
        this(pattern, actionPath, "");
    }

    public DynamicTaskGenericRoute(String pattern, ActionPath actionPath, String processor) {
        super(pattern, actionPath, processor, null);
    }

    /**
     * @param given current parameter
     * @param pathElement  pattern element
     * @return true, match
     */
    protected boolean matchElement(String given, PathElement pathElement) {
        String expected = pathElement.getString();

        if (pathElement.isParameter()) {
            if (!handleParameter(given, pathElement.getParamName()))
                return false;
        }
        else if ("[task]".equals(expected)) {
            if (!handleTask(given))
                return false;
        }
        else if (!given.equals(expected))
            return false;

        return true;
    }

    /**
     * @param currentValue  current value passed via URL
     * @return true, if task was valid
     */
    private boolean handleTask(String currentValue) {
        Matcher m = validTaskPattern.matcher(currentValue);
        if (!m.matches())
            return false;

        this.task = currentValue;
        return true;
    }

    public String getTask() {
        return task;
    }
}
