/*
 * ActionPath.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * qualified action name with full path
 */
public class ActionPath {
    private static final Pattern pathPattern = Pattern.compile("/[a-zA-Z0-9_\\./-]*[a-zA-Z0-9_\\.-]");

    private final String path;

    public ActionPath(String path) {
        this.path = filterPath(path);
    }

    public ActionPath(AreaPath path, String actionBaseName) {
        StringBuilder b = new StringBuilder();
        for (String element: path.getAreas()) {
            b.append("/").append(element);
        }
        b.append("/").append(actionBaseName);

        this.path = filterPath(b.toString());
    }

    private static String filterPath(String path) {
        String p = path.trim();
        Matcher m = pathPattern.matcher(p);
        if (!m.matches())
            throw new IllegalArgumentException("invalid action path");

        return p;
    }

    public String getPath() {
        return path;
    }

    public AreaPath getAreaPath() {
        String[] splitted = path.split("/");

        List<String> areas = new Vector<>();
        for (int i=0; i<splitted.length-1; i++) {
            String e = splitted[i];
            if (e.length() > 0) {
                areas.add(e);
            }
        }

        return new AreaPath(areas);
    }

    public String getActionName() {
        String[] splitted = path.split("/");
        return splitted[splitted.length-1];
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionPath)) return false;

        ActionPath that = (ActionPath) o;

        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
