/*
 * FeedAction.java
 *
 * Copyright 2009-2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import de.highbyte_le.weberknecht.feeds.FeedData;
import de.highbyte_le.weberknecht.request.DatabaseCapable;

/**
 * webapp actions producing feeds (RSS/Atom)
 * 
 * @author pmairif
 */
public interface FeedAction extends DatabaseCapable, ExecutableAction {
	/**
	 * necessary data to produce feeds
	 * @return feed model
	 */
	public FeedData getFeedModel();
}
