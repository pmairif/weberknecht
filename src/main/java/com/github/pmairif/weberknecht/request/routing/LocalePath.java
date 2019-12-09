/*
 * LocalePath.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import java.util.Locale;

/**
 * holding path with extracted locale
 *
 * @author pmairif
 */
public class LocalePath {
	private final AreaPath path;
	
	private final Locale locale;

	public LocalePath(String[] pathSegments, Locale locale) {
		this.path = new AreaPath();
		if (pathSegments != null) {
			for (String s: pathSegments)
				this.path.addPath(s);
		}
		
		this.locale = locale;
	}
	
	public LocalePath(AreaPath areapath, Locale locale) {
		this.path = areapath;
		this.locale = locale;
	}
	
	public LocalePath(AreaPath areapath) {
		this(areapath, null);
	}
	
	public LocalePath() {
		this(new AreaPath(), null);
	}
	
	/**
	 * @return the path
	 */
	public AreaPath getPath() {
		return path;
	}
	
	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LocalePath{");
        sb.append("path=").append(path);
        sb.append(", locale=").append(locale);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalePath)) return false;

        LocalePath that = (LocalePath) o;

        if (!path.equals(that.path)) return false;
        return !(locale != null ? !locale.equals(that.locale) : that.locale != null);

    }

    @Override
    public int hashCode() {
        int result = path.hashCode();
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        return result;
    }
}
