/*
 * DbConnectionHolderTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.db;

import com.github.pmairif.weberknecht.conf.ConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author pmairif
 */
class DbConnectionHolderTest {

	private DbConnectionHolder holder;
	
	private DbConnectionProvider provider;
	
	private Connection con;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.provider = mock(DbConnectionProvider.class);
		this.holder = new DbConnectionHolder(provider);
		
		this.con = mock(Connection.class);
		when(this.provider.getConnection()).thenReturn(con);
	}

	/**
	 * Test method for {@link DbConnectionHolder#getConnection()}.
	 */
	@Test
	public void testGetConnectionOnceCalled() throws DBConnectionException, ConfigurationException {
		holder.getConnection();
		verify(provider, times(1)).getConnection();
	}

	@Test
	public void testGetConnectionTwiceCalled() throws DBConnectionException, ConfigurationException {
		holder.getConnection();
		holder.getConnection();
		verify(provider, times(1)).getConnection();
	}
	
	@Test
	public void testGetConnectionNotCalled() throws DBConnectionException {
		verify(provider, times(0)).getConnection();
	}

	/**
	 * Test method for {@link DbConnectionHolder#close()}.
	 */
	@Test
	public void testCloseWithoutConnection() throws SQLException {
		holder.close();
		verify(con, times(0)).close();
	}

	@Test
	public void testCloseWithConnection() throws SQLException, DBConnectionException, ConfigurationException {
		holder.getConnection();
		holder.close();
		verify(con, times(1)).close();
	}

	@Test
	void testGetConnectionWithoutConnectionProvider() {
		final DbConnectionHolder dbConnectionHolder = new DbConnectionHolder(null);
		assertThrows(ConfigurationException.class, dbConnectionHolder::getConnection);
	}
}
