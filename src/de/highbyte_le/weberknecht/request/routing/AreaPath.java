/*
 * AreaPath.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import java.util.List;
import java.util.Vector;

/**
 * path of areas to access the action
 *
 * @author rick
 */
public class AreaPath implements Cloneable {

	private final List<String> areas;
	
	public AreaPath() {
		this.areas = new Vector<String>();
	}

	public AreaPath(String...areas) {
		this.areas = new Vector<String>(areas.length);
		for (String area: areas) {
			addPath(area);
		}
	}
	
	public AreaPath addPath(String area) {
		if (area != null && area.length() > 0)
			this.areas.add(area);
		
		return this;
	}
	
	/**
	 * @return the areas
	 */
	public List<String> getAreas() {
		return areas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areas == null) ? 0 : areas.hashCode());
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
		if (areas == null) {
			if (other.areas != null)
				return false;
		}
		else if (!areas.equals(other.areas))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AreaPath [areas=" + areas + "]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public AreaPath clone() {
		AreaPath newPath = new AreaPath();
		
		for (String s: areas)
			newPath.areas.add(s);
		
		return newPath;
	}
}
