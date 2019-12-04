/*
 * View.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request;

import java.net.URL;

/**
 * information of webapp views 
 * @author pmairif
 */
public class View {
	private String jspFileName = null;
	
	private URL redirection = null;

	/**
	 * @param jspFileName
	 * 		the full file name with extension but without path of the JSP file used as view
	 */
	public View(String jspFileName) {
		this.jspFileName = jspFileName;
	}

	/**
	 * @param redirection
	 */
	public View(URL redirection) {
		this.redirection = redirection;
	}

	public boolean hasJspFileName() {
		return (this.jspFileName != null && this.jspFileName.length() > 0);
	}

	/**
	 * JSP file name
	 * @return
	 * 		the full file name with extension but without path of the JSP file used as view
	 */
	public String getJspFileName() {
		return this.jspFileName;
	}

	public boolean hasRedirection() {
		return (this.redirection != null);
	}
	
	public URL getRedirection() {
		return this.redirection;
	}
}
