/*
 * JsonView.java
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.view;

import org.json.JSONException;
import org.json.JSONWriter;

/**
 * webapp actions producing JSON data (see http://www.json.org/)
 * 
 * @author pmairif
 */
public interface JsonView {
	/**
	 * create the JSON data.
	 * 
	 * <p>Use {@link JSONWriter#array()} or {@link JSONWriter#object()} to add data.
	 * Finish with {@link JSONWriter#endArray()} or {@link JSONWriter#endObject()}.</p>
	 */
	public void writeJson(JSONWriter writer) throws JSONException;
}
