/*
 * DefaultWebDbConnectionProvider.java
 * 
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=utf-8, tabstob=4
 */
package com.github.pmairif.weberknecht.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * provide db connections that are configured via JNDI
 *
 * @author pmairif
 */
@SuppressWarnings("nls")
public class DefaultWebDbConnectionProvider implements DbConnectionProvider  {
	private DbType type;

	/**
	 * Logger for this class
	 */
	private final Logger logger = LoggerFactory.getLogger(DefaultWebDbConnectionProvider.class);

	public DefaultWebDbConnectionProvider() {
        this.type = DbType.MYSQL;	//FIXME implement some property handling for db type
	}

	public Connection getConnection() throws DBConnectionException {
        try {
        	
            //JNDI-Context
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");

            //JNDI-Lookup for jdbc connection
            DataSource ds = (DataSource) envCtx.lookup("jdbc/mydb");
            Connection con = ds.getConnection();

            return con;
            
        }
        catch (NamingException e) {
            logger.error("NamingException: "+e.getMessage(), e);
            throw new DBConnectionException("database connection is not yet configured (naming exception)", e);
        }
        catch (SQLException e) {
            logger.error("SQLException: "+e.getMessage(), e);
            throw new DBConnectionException("cannot connect to database (sql exception)", e);
        }        

	}

	public boolean isAvailable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see DbConnectionProvider#getType()
	 */
	@Override
	public DbType getType() {
		return type;
	}
}
