/*
 * FeedData.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 08.02.2009
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.feeds;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * data that can be put into a feed
 * 
 * @author pmairif
 */
public class FeedData {

	private String title = null;
	
	private String description = null;
	
	/**
	 * The URL to the HTML website corresponding to the channel.
	 */
	private String url = null;
	
	/**
	 * The URL to the feed itself
	 */
	private String selfLink = null;
	
	private String language = null;
	
	private String copyright = null;
	
	/**
	 * The publication date for the content in the channel.
	 * 
	 * <p>For example, the New York Times publishes on a daily basis, the publication date flips once every 24 hours.
	 * That's when the pubDate of the channel changes. </p> 
	 */
	private Date pubDate = null;
	
	/**
	 * The last time the content of the channel changed.
	 */
	private Date lastBuildDate = null;
	
	private List<FeedEntry> entries = new Vector<FeedEntry>();
	
	public FeedData(String title, String url, String selfLink) {
		this.title = title;
		this.url = url;
		this.selfLink = selfLink;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the entries
	 */
	public List<FeedEntry> getEntries() {
		return this.entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<FeedEntry> entries) {
		this.entries = entries;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the selfLink
	 */
	public String getSelfLink() {
		return this.selfLink;
	}
	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the copyright
	 */
	public String getCopyright() {
		return this.copyright;
	}

	/**
	 * @param copyright the copyright to set
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * @return the pubDate
	 */
	public Date getPubDate() {
		return this.pubDate;
	}

	/**
	 * @param pubDate the pubDate to set
	 */
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	/**
	 * @return the lastBuildDate
	 */
	public Date getLastBuildDate() {
		return this.lastBuildDate;
	}

	/**
	 * @param lastBuildDate the lastBuildDate to set
	 */
	public void setLastBuildDate(Date lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}
}
