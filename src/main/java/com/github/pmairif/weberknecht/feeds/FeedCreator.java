/*
 * FeedCreator.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 2009-02-09
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.feeds;

import org.jdom2.Document;

/**
 * create feeds
 * 
 * @author pmairif
 */
public interface FeedCreator {
	public Document createFeed(FeedData feedData);
	
	public String getContentType();
}
