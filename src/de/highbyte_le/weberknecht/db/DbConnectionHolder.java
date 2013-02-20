/*
 * DbConnectionHolder.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.db;

import java.sql.Connection;
import java.sql.SQLException;

import de.highbyte_le.weberknecht.conf.ConfigurationException;

/**
 * retrieves and holds db connections on demand. If a connection is requested twice, it is reused.
 *
 * @author pmairif
 */
public class DbConnectionHolder {
	private final DbConnectionProvider dbConnectionProvider;
	
	private Connection con = null;
	
	public DbConnectionHolder(DbConnectionProvider dbConnectionProvider) {
		this.dbConnectionProvider = dbConnectionProvider;
	}

	public synchronized Connection getConnection() throws DBConnectionException, ConfigurationException {
		if (null == dbConnectionProvider)
			throw new ConfigurationException("missing db connection provider. probably no database configured");
		
		if (null == con)
			 con = dbConnectionProvider.getConnection();
		
		return con;
	}
	
	public synchronized void close() throws SQLException {
		if (con != null)
			con.close();
		
		con = null;
	}
}
