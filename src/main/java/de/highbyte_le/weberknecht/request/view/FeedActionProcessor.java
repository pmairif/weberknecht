/*
 * FeedActionProcessor.java (weberknecht)
 *
 * Copyright 2010-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.view;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.highbyte_le.weberknecht.feeds.FeedCreator;
import de.highbyte_le.weberknecht.feeds.FeedData;
import de.highbyte_le.weberknecht.feeds.RssCreator;
import de.highbyte_le.weberknecht.request.Executable;

/**
 * Process feed views
 * 
 * @author pmairif
 */
public class FeedActionProcessor implements ActionViewProcessor {
	
	private final Log log = LogFactory.getLog(FeedActionProcessor.class);
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionProcessor#processAction(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, de.highbyte_le.weberknecht.request.ExecutableAction)
	 */
	@Override
	public boolean processView(HttpServletRequest request, HttpServletResponse response, Executable action) throws IOException {
		if (action instanceof FeedView)
			return processView(request, response, (FeedView)action);

		throw new IllegalArgumentException("Action not applicable here.");
	}
	
	public boolean processView(HttpServletRequest request, HttpServletResponse response, FeedView action) throws IOException {
		if (log.isDebugEnabled())
			log.debug("processView() - processing action "+action.getClass().getSimpleName());
		
		FeedData feedData = action.getFeedModel();
		
		//display feed
		FeedCreator feedCreator = new RssCreator();
		Document doc = feedCreator.createFeed(feedData);
		
		XMLOutputter xmlOutputter = new XMLOutputter();
		Format f = xmlOutputter.getFormat();
		f.setIndent("\t");
		xmlOutputter.setFormat(f);

		response.setContentType( feedCreator.getContentType() );
		OutputStream out = response.getOutputStream();
		try {
			xmlOutputter.output(doc, out);
		}
		finally {
			out.close();
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.ActionViewProcessor#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		//
	}

	/* (non-Javadoc)
	 * @see de.highbyte_le.weberknecht.request.view.ActionViewProcessor#setActionViewProcessorFactory(de.highbyte_le.weberknecht.request.view.ActionViewProcessorFactory)
	 */
	@Override
	public void setActionViewProcessorFactory(ActionViewProcessorFactory factory) {
		//
	}
}
