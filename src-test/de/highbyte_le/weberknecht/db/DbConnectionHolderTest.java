/*
 * DbConnectionHolderTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.db;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author pmairif
 */
public class DbConnectionHolderTest {

	private DbConnectionHolder holder;
	
	private DbConnectionProvider provider;
	
	private Connection con;
	
	@Before
	public void setUp() throws Exception {
		this.provider = mock(DbConnectionProvider.class);
		this.holder = new DbConnectionHolder(provider);
		
		this.con = mock(Connection.class);
		when(this.provider.getConnection()).thenReturn(con);
	}

	/**
	 * Test method for {@link de.highbyte_le.weberknecht.db.DbConnectionHolder#getConnection()}.
	 */
	@Test
	public void testGetConnectionOnceCalled() throws DBConnectionException {
		holder.getConnection();
		verify(provider, times(1)).getConnection();
	}

	@Test
	public void testGetConnectionTwiceCalled() throws DBConnectionException {
		holder.getConnection();
		holder.getConnection();
		verify(provider, times(1)).getConnection();
	}
	
	@Test
	public void testGetConnectionNotCalled() throws DBConnectionException {
		verify(provider, times(0)).getConnection();
	}

	/**
	 * Test method for {@link de.highbyte_le.weberknecht.db.DbConnectionHolder#close()}.
	 */
	@Test
	public void testCloseWithoutConnection() throws SQLException {
		holder.close();
		verify(con, times(0)).close();
	}

	@Test
	public void testCloseWithConnection() throws SQLException, DBConnectionException {
		holder.getConnection();
		holder.close();
		verify(con, times(1)).close();
	}
	
}
