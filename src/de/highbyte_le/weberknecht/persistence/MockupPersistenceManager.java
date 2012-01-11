/*
 * MockupPersistenceManager.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 30.01.2009
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.persistence;

import java.util.HashMap;
import java.util.Map;

import de.highbyte_le.weberknecht.model.GeneralUserBean;

/**
 * Visitor persistence manager mock.
 * 
 * @author pmairif
 */
public class MockupPersistenceManager implements VisitorPersistenceManager {
	
	private static int maxId = 0;
	
	private Map<Integer, GeneralUserBean> localIdUserMap = new HashMap<Integer, GeneralUserBean>();

	/**
	 * mapping of global to local IDs
	 */
	private Map<String, Integer> globalIdMap = new HashMap<String, Integer>();

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.persistence.VisitorPersistenceManager#fetchVisitor(int)
	 */
	@Override
	public GeneralUserBean fetchVisitor(int visitorId) {
		return localIdUserMap.get(new Integer(visitorId));
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.persistence.VisitorPersistenceManager#getVisitorId(java.lang.String)
	 */
	@Override
	public int getVisitorId(String globalIdentifier) {
		Integer v = globalIdMap.get(globalIdentifier);
		if (v != null)
			return v.intValue();
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.persistence.VisitorPersistenceManager#storeVisitor(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public synchronized int storeVisitor(String globalIdentifier, String name, String email, String verifiedEmail, String url) {
	
		int id = 0;
		
		if (globalIdMap.containsKey(globalIdentifier)) {
			id = globalIdMap.get(globalIdentifier).intValue();
		}
		else {
			id = nextId();

			GeneralUserBean ub = new GeneralUserBean();
			ub.setIdentifier(globalIdentifier);
			ub.setDisplayName(name);
			ub.setEmail(email);
			ub.setVerifiedEmail(verifiedEmail);
			ub.setUrl(url);
			ub.setId(id);
	
			localIdUserMap.put(new Integer(id), ub);
			globalIdMap.put(globalIdentifier, new Integer(id));
		}

		return id;
	}

	protected static synchronized int nextId() {
		maxId++;
		return maxId;
	}
}
