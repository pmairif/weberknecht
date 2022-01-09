/*
 * RssCreator.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 2009-02-09
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.feeds;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * create RSS 2.0 feeds
 * 
 * @author pmairif
 */
public class RssCreator implements FeedCreator {
	
	/* (non-Javadoc)
	 * @see FeedCreator#createFeed(FeedData)
	 */
	@Override
	public Document createFeed(FeedData feedData) {
		Document doc = new Document();
		
		Namespace atomNs = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");
		
		Element rootElement = new Element("rss");
		rootElement.setAttribute("version", "2.0");
		rootElement.addNamespaceDeclaration(atomNs);
		doc.setRootElement(rootElement);
		
		Element channelElement = new Element("channel");
		rootElement.addContent(channelElement);
		
		Element selfLink = new Element("link", atomNs);
		selfLink.setAttribute("href", feedData.getSelfLink());
		selfLink.setAttribute("rel", "self");
		selfLink.setAttribute("type", "application/rss+xml");
		channelElement.addContent(selfLink);
		
		
		Element titleElement = new Element("title").setText( feedData.getTitle() );
		channelElement.addContent(titleElement);
		
		Element linkElement = new Element("link").setText(feedData.getUrl());
		channelElement.addContent(linkElement);
		
		Element descElement = new Element("description").setText(feedData.getDescription());
		channelElement.addContent(descElement);
		
		DateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		for (FeedEntry entry: feedData.getEntries()) {
			Element itemElement = new Element("item");
			channelElement.addContent(itemElement);
			
			Element entryGuidElement = new Element("guid").setText(entry.getId());
			itemElement.addContent(entryGuidElement);
			
			Element entryTitleElement = new Element("title").setText(entry.getTitle());
			itemElement.addContent(entryTitleElement);
			
			Element entryLinkElement = new Element("link").setText(entry.getUrl());
			itemElement.addContent(entryLinkElement);
			
			Element entryDescElement = new Element("description").setText(entry.getDescription());
			itemElement.addContent(entryDescElement);
			
			if (entry.getPubDate() != null) {
				Element enryPubDateElement = new Element("pubDate").setText( df.format(entry.getPubDate()) );
				itemElement.addContent(enryPubDateElement);
			}

			if (entry.getAuthor() != null) {
				Element entryAuthorElement = new Element("author").setText(entry.getAuthor());
				itemElement.addContent(entryAuthorElement);
			}
		}
		
		return doc;
	}

	/* (non-Javadoc)
	 * @see FeedCreator#getContentType()
	 */
	@Override
	public String getContentType() {
		return "application/rss+xml";
//		return "text/xml";
	}
}
