/*
 * PathElement.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing.flex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rick on 2015-05-16.
 */
class PathElement {
    protected static final Pattern parameterPattern = Pattern.compile("\\{([a-zA-Z0-9_-]+)\\}");

    /**
     * the original pattern string
     */
    private final String string;

    private final String paramName;

    private final boolean parameter;

    public PathElement(String string) {
        this.string = string;

        Matcher m = parameterPattern.matcher(string);
        parameter = (m.matches());

        if (parameter)
            paramName = m.group(1);
        else
            paramName = null;
    }

    public String getString() {
        return string;
    }

    public boolean isParameter() {
        return parameter;
    }

    public String getParamName() {
        return paramName;
    }
}
