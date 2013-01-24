/*
 * RoutingLocalePrefix.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.conf;

import java.util.List;
import java.util.Vector;

/**
 * locale prefix configuration
 *
 * @author pmairif
 */
public class RoutingLocalePrefix {

	private final List<String> allowedLocales = new Vector<String>();
	
	private boolean optional = false;
	
	public RoutingLocalePrefix() {
		//
	}
	
	public RoutingLocalePrefix(boolean optional, String...allowedLocales) {
		this.optional = optional;
		
		for (String a: allowedLocales)
			this.allowedLocales.add(a);
	}

	public void addAllowedLocale(String locale) {
		this.allowedLocales.add(locale);
	}
	
	/**
	 * @return the allowedLocales
	 */
	public List<String> getAllowedLocales() {
		return allowedLocales;
	}

	/**
	 * @param optional the optional to set
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	
	/**
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allowedLocales == null) ? 0 : allowedLocales.hashCode());
		result = prime * result + (optional ? 1231 : 1237);
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
		RoutingLocalePrefix other = (RoutingLocalePrefix) obj;
		if (allowedLocales == null) {
			if (other.allowedLocales != null)
				return false;
		}
		else if (!allowedLocales.equals(other.allowedLocales))
			return false;
		if (optional != other.optional)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoutingLocalePrefix [allowedLocales=" + allowedLocales + ", optional=" + optional + "]";
	}
	
}
