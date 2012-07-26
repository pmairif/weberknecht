/*
 * FeedAction.java
 *
 * Copyright 2009-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import de.highbyte_le.weberknecht.feeds.FeedData;

/**
 * webapp actions producing feeds (RSS/Atom)
 * 
 * @author pmairif
 */
public interface FeedAction extends ExecutableAction {
	/**
	 * necessary data to produce feeds
	 * @return feed model
	 */
	public FeedData getFeedModel();
}
