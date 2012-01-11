/*
 * DbType.java
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 2009-04-16
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.db;

/**
 * database system type
 * @author pmairif
 */
public enum DbType {
	/**
	 * MySQL
	 */
	MYSQL,
	
	/**
	 * PostgreSQL
	 */
	POSTGRES,
	
	/**
	 * no real database
	 */
	DUMMY;
	
	public static DbType parseUrl(String url) {
		if (url.startsWith("jdbc:postgresql:"))
			return POSTGRES;
		if (url.startsWith("jdbc:mysql:"))
			return MYSQL;
		return null;
	}
}
