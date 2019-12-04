/*
 * DatabaseCapable.java
 *
 * Copyright 2009-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

import java.sql.Connection;

/**
 * Capable of handling a database.
 * 
 * <p>Does this action need a database connection?
 * The controller will only connect to the database, if this interface is implemented.
 * On the other hand, an exception will be thrown, if this interface is implemented
 * but the DB connection is not configured. </p>
 * 
 * @author pmairif
 */
public interface DatabaseCapable {
	/**
	 * set database connection
	 */
	void setDatabase(Connection con);
}
