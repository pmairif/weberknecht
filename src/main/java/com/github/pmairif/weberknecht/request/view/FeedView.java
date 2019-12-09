/*
 * FeedView.java
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.view;

import com.github.pmairif.weberknecht.feeds.FeedData;

/**
 * producing feeds (RSS/Atom)
 * 
 * @author pmairif
 */
public interface FeedView {
	/**
	 * necessary data to produce feeds
	 * @return feed model
	 */
	public FeedData getFeedModel();
}
