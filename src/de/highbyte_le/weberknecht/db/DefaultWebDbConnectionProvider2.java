/*
 * DefaultWebDbConnectionProvider2.java
 * 
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=utf-8, tabstob=4
 */
package de.highbyte_le.weberknecht.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * provide DB connection from within web container.
 * 
 * <p>This connection provider throws {@link NamingException}s, if the connection is not configured.</p>
 * 
 * @author pmairif
 */
@SuppressWarnings("nls")
public class DefaultWebDbConnectionProvider2 implements DbConnectionProvider {
	private DataSource datasource = null;
	
	private DbType type;

	private int onErrorRetryCount = 5;
	
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(DefaultWebDbConnectionProvider2.class);

	public DefaultWebDbConnectionProvider2(String jdbcContextName) throws NamingException {
    	//JNDI-Context
        Context ctx = new InitialContext();
        Context envCtx = (Context) ctx.lookup("java:comp/env");

        //JNDI-Lookup for jdbc connection
        this.datasource = (DataSource) envCtx.lookup(jdbcContextName);
        
        this.type = DbType.MYSQL;	//FIXME implement some property handling for db type
	}

	public DefaultWebDbConnectionProvider2(DataSource dataSource, DbType type) {
        this.datasource = dataSource;
        this.type = type;
	}

	/**
	 * is the database connection available, i. e. properly configured?
	 * @return true, if it is available
	 */
	@Override
	public boolean isAvailable() {
		return (this.datasource != null);
	}

	@Override
	public Connection getConnection() throws DBConnectionException {
        try {
        	
        	if (!isAvailable())
        		throw new DBConnectionException("db connection is not available (e.g. not configured)");

        	Connection con = null;
        	boolean ok = false;
        	int count = 0;
        	while (!ok && count < onErrorRetryCount) {
        		count++;
            	con = this.datasource.getConnection();

        		try {
		        	Statement st = con.createStatement();
		        	ResultSet rs = st.executeQuery("select true");
		        	if (rs.next())
		        		ok = true;
	        	}
	        	catch (SQLException e) {
	        		log.warn("SQL exception while testing connection (attempt "+count+"): "+e.getMessage());
	        		try { con.close(); } catch (SQLException e1){/**/}
	        		//and again
	        	}
        	}
        	
        	return con;

        }
        catch (SQLException e) {
            log.error("getConnection() - SQLException: "+e.getMessage(), e);
            throw new DBConnectionException("sql exception: "+e.getMessage());
        }        
	}

	@Override
	public DbType getType() {
		return type;
	}
	
	/**
	 * @return the onErrorRetryCount
	 */
	public int getOnErrorRetryCount() {
		return this.onErrorRetryCount;
	}

	/**
	 * @param onErrorRetryCount the onErrorRetryCount to set
	 */
	public void setOnErrorRetryCount(int onErrorRetryCount) {
		this.onErrorRetryCount = onErrorRetryCount;
	}
}
