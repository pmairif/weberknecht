/*
 * GenericFormBean.java (weberknecht)
 * 
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=UTF-8, tabstob=4
 */
package de.highbyte_le.weberknecht.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.highbyte_le.weberknecht.util.TextFormat;

/**
 * hold data for web forms
 * @author pmairif
 */
public class GenericFormBean implements Serializable {
	private Map<String, String> values = new HashMap<String, String>();
	
	private int id = 0;
	
	private static final DateFormat isoDf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final long serialVersionUID = 6649548599811044535L;
	
	private final Log logger = LogFactory.getLog(GenericFormBean.class);

	public GenericFormBean() {
		//
	}
	
	public void populate(@SuppressWarnings("hiding") int id) {
		this.id = id;
	}

	public void populate(@SuppressWarnings("hiding") int id, Map<String, String> valueMap) {
		this.id = id;
		this.values.putAll(valueMap);
	}
	
	public void putValue(String fieldName, String value) {
		values.put(fieldName, value);
	}
	
	public String getValue(String fieldName) {
		String val = values.get(fieldName);
		if (null == val)
			val = "";
		return val;
	}

	public String getValue_htmlEscaped(String fieldName) {
		return StringEscapeUtils.escapeHtml(getValue(fieldName));
	}

	/**
	 * convert line breaks etc.
	 */
	public String getValue_htmlFormatted(String fieldName) {
		String text = getValue_htmlEscaped(fieldName);
		return TextFormat.getTextHtmlFormatted(text);
	}

	public int getValueAsInt(String fieldName) {
		String valString = values.get(fieldName);

		int val = 0;
		
		if (valString != null) {
			try {
				val = Integer.parseInt(valString);
			}
			catch (NumberFormatException e) {
				logger.warn("number format exception while parsing field '"+fieldName+"' with value '"+valString+"' as int: "+e.getMessage());
			}
		}
		
		return val;
	}

	public Date getValueAsDate(String fieldName) {
		String valString = values.get(fieldName);

		Date val = null;
		
		if (valString != null) {
			try {
				val = isoDf.parse(valString);
			}
			catch (ParseException e) {
				logger.warn("parse exception while parsing field '"+fieldName+"' with value '"+valString+"' as Date: "+e.getMessage());
			}
		}
		
		return val;
	}

	public int getId() {
		return id;
	}
}
