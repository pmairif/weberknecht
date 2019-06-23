/*
 * AreaPath.java (weberknecht)
 *
 * Copyright 2013-2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * parsed URL path
 *
 * @author pmairif
 */
public class AreaPath implements Cloneable {

	private final List<String> elements;
	
	public AreaPath() {
		this.elements = new Vector<>();
	}

	public AreaPath(List<String> elements) {
		this.elements = new Vector<>(elements);
	}
	
	public AreaPath(String... elements) {
		this.elements = new Vector<>(elements.length);
		for (String area: elements) {
			addPath(area);
		}
	}
	
	public AreaPath addPath(String area) {
		if (area != null && area.length() > 0)
			this.elements.add(area);
		
		return this;
	}
	
	/**
	 * create copy of current instance and add sub path to it.
	 */
	public AreaPath fork(String area) {
		return clone().addPath(area);
	}
	
	/**
	 * @return the elements
	 */
	public List<String> getAreas() {
		return elements;
	}

    public int size() {
        return elements.size();
    }

    public Iterator<String> iterator() {
        return elements.iterator();
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + elements.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AreaPath other = (AreaPath) obj;
        return elements.equals(other.elements);
    }

	@Override
	public String toString() {
		return "AreaPath [elements=" + elements + "]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public AreaPath clone() {
		AreaPath newPath = new AreaPath();
		
		for (String s: elements)
			newPath.elements.add(s);
		
		return newPath;
	}
}
