/*
 * GenericRoute.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing.flex;

import de.highbyte_le.weberknecht.request.routing.ActionPath;
import de.highbyte_le.weberknecht.request.routing.AreaPath;

import java.util.*;

/**
 * routing definition entity
 */
public class GenericRoute implements Route {
    /**
     * url pattern
     */
    private final String pattern;

    /**
     * splitted url pattern
     */
    private final List<PathElement> parts;

    private final Map<String, ParameterParser<?>> parameterMap = new HashMap<>();

    /**
     * parsed parameter values
     */
    private final Map<String, Object> parameterValues = new HashMap<>();

    /**
     * target action path
     */
    private final ActionPath actionPath;

    /**
     * optional target action task
     */
    private final String task;

    /**
     * optional target action processor
     */
    private final String processor;

    public GenericRoute(String pattern, ActionPath actionPath) {
        this(pattern, actionPath, "", null);
    }

    public GenericRoute(String pattern, ActionPath actionPath, String processor, String task) {
        this.pattern = pattern;
        this.actionPath = actionPath;
        this.processor = processor;
        this.task = task;

        String[] splittedPattern = pattern.split("/");
        this.parts = new Vector<>(splittedPattern.length);
        for (String s: splittedPattern) {
            this.parts.add(new PathElement(s));
        }
    }

    public void addParameterParser(String name, ParameterParser<?> parameterParser) {
        this.parameterMap.put(name, parameterParser);
    }

    public boolean match(AreaPath path) {
        if (path.size() != parts.size())
            return false;

        Iterator<String> pathIt = path.iterator();
        Iterator<PathElement> partIt = parts.iterator();
        while (pathIt.hasNext() && partIt.hasNext()) {
            String given = pathIt.next();
            PathElement expected = partIt.next();

            if (!matchElement(given, expected)) return false;
        }

        return true;
    }

    /**
     * @param given current parameter
     * @param pathElement  pattern element
     * @return true, match
     */
    protected boolean matchElement(String given, PathElement pathElement) {
        if (pathElement.isParameter()) {
            if (!handleParameter(given, pathElement.getParamName()))
                return false;
        }
        else if (!given.equals(pathElement.getString()))
            return false;

        return true;
    }

    /**
     * @param currentValue  current value passed via URL
     * @param param name of the parameter to parse
     * @return true, if parameter was valid
     */
    protected boolean handleParameter(String currentValue, String param) {
        ParameterParser<?> parser = parameterMap.get(param);
        if (null == parser)
            throw new IllegalArgumentException("missing parser for "+param);

        Object parsedValue = parser.parse(currentValue);
        parameterValues.put(param, parsedValue);

        return parsedValue != null;
    }

    public Map<String, Object> getParameterValues() {
        return parameterValues;
    }

    public ActionPath getActionPath() {
        return actionPath;
    }

    public String getProcessor() {
        return processor;
    }

    public String getTask() {
        return task;
    }

    @Override
    public String toString() {
        return pattern;
    }
}
