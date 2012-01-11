/*
 * DatabaseCapable.java
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

import java.sql.Connection;

/**
 * Capable of handling a database.
 * 
 * @author pmairif
 */
public interface DatabaseCapable {
	/**
	 * set database connection
	 */
	public void setDatabase(Connection con);
	
	/**
	 * does this action need a database connection?
	 * 
	 * <p>The controller will only connect to the database, if this method returns true.
	 * On the other hand, an exception will be thrown, if this method returns true,
	 * but the DB connection is not configured. </p>
	 * 
	 * @return true, if DB connection is needed
	 */
	public boolean needsDatabase();
}