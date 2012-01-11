/*
 * DbConnectionProvider.java
 *
 * Copyright 2007 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=utf-8, tabstop=4
 */
package de.highbyte_le.weberknecht.db;

import java.sql.Connection;

/**
 * @author pmairif
 */
public interface DbConnectionProvider {
	/**
	 * create new db connection or get one from pool.
	 * 
	 * The connection should be closed after usage.
	 *  
	 * @throws DBConnectionException
	 */
	public Connection getConnection() throws DBConnectionException;
	
	/**
	 * is the db connection available, i. e. properly configured?
	 * @return true, if it is available
	 */
	public boolean isAvailable();

	public DbType getType();
}
