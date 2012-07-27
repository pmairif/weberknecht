/*
 * VisitorPersistenceManager.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 29.01.2009
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.persistence;

import de.highbyte_le.weberknecht.model.GeneralUserBean;

/**
 * interface for storing and fetching visitor/user data
 * @author pmairif
 */
public interface VisitorPersistenceManager {
	/**
	 * search Visitor by it's global Identifier
	 * @param globalIdentifier
	 * @return local id of the visitor or 0, if not found 
	 */
	public int getVisitorId(String globalIdentifier) throws DataAccessException;
	
	/**
	 * creates or updates a visitor dataset depending on the global identifier
	 */
	public int storeVisitor(String globalIdentifier, String name, String email, String verifiedEmail, String url) throws DataAccessException;
	
	/**
	 * 
	 * @param visitorId
	 * @return
	 * 		null, if id not found
	 */
	public GeneralUserBean fetchVisitor(int visitorId) throws DataAccessException;
}
