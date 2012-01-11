/*
 * JsonAction.java
 *
 * Copyright 2009-2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import org.json.JSONException;
import org.json.JSONWriter;

import de.highbyte_le.weberknecht.request.DatabaseCapable;

/**
 * webapp actions producing JSON data (see http://www.json.org/)
 * 
 * @author pmairif
 */
public interface JsonAction extends DatabaseCapable, ExecutableAction {
	/**
	 * create the JSON data.
	 * 
	 * <p>Use {@link JSONWriter#array()} or {@link JSONWriter#object()} to add data.
	 * Finish with {@link JSONWriter#endArray()} or {@link JSONWriter#endObject()}.</p>
	 */
	public void writeJson(JSONWriter writer) throws JSONException;
}
