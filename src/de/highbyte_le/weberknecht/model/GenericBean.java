/*
 * GenericBean.java (weberknecht)
 * 
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=UTF-8, tabstob=4
 */
package de.highbyte_le.weberknecht.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import de.highbyte_le.weberknecht.util.TextFormat;

/**
 * hold data for web pages.
 * 
 * <p>This could be a replacement for {@link GenericFormBean} and some of your hand made page beans.</p>  
 * 
 * @see GenericFormBean
 * @see GenericListBean
 * @author pmairif
 */
public class GenericBean<T> implements Serializable {

	private Map<T, String> values = new HashMap<T, String>();
	
//	private Map<T, List<GenericBean<T>>> lists = new HashMap<T, List<GenericBean<T>>>();
	
	private static final long serialVersionUID = -2587187021195865174L;
	
//	private final Log logger = LogFactory.getLog(GenericBean.class);

	public GenericBean() {
		//
	}

	public GenericBean(Map<T, String> valueMap) {
		populate(valueMap);
	}

//	public GenericBean(Map<T, String> valueMap, Map<T, GenericListBean<T>> listMap) {
//		populate(valueMap, listMap);
//	}

	public void populate(Map<T, String> valueMap) {
		this.values.putAll(valueMap);
	}

//	public void populate(Map<T, String> valueMap, Map<T, GenericListBean<T>> listMap) {
//		this.values.putAll(valueMap);
//		this.lists.putAll(listMap);
//	}

	public void putValue(T fieldName, String value) {
		values.put(fieldName, value);
	}

	public void putValue(T fieldName, int value) {
		values.put(fieldName, Integer.toString(value));
	}

	/**
	 * check, if value is present
	 */
	public boolean hasValue(T field) {
		return values.containsKey(field) && values.get(field) != null;
	}

	/**
	 * get value
	 */
	public String getValue(T field) {
		String val = values.get(field);
		if (null == val)
			val = "";
		return val;
	}

	/**
	 * get value HTML escaped
	 */
	public String getValue_htmlEscaped(T field) {
		return StringEscapeUtils.escapeHtml(getValue(field));
	}

	/**
	 * convert line breaks etc.
	 */
	public String getValue_htmlFormatted(T field) {
		String text = getValue_htmlEscaped(field);
		return TextFormat.getTextHtmlFormatted(text);
	}

//	public void putList(T fieldName, GenericListBean<T> list) {
//		lists.put(fieldName, list);
//	}
//
//	/**
//	 * check, if list is present
//	 */
//	public boolean hasList(T field) {
//		return lists.containsKey(field);
//	}
//
//	/**
//	 * get list
//	 */
//	public GenericListBean<T> getList(T field) {
//		if (hasList(field))
//			return lists.get(field);
//		return new GenericListBean<T>();
//	}

	/**
	 * check, if value is present
	 */
	public boolean has(T field) {
		return hasValue(field);
	}

	/**
	 * get value
	 */
	public String val(T field) {
		return getValue(field);
	}

	/**
	 * get value HTML escaped
	 */
	public String valHtmlEsc(T field) {
		return getValue_htmlEscaped(field);
	}

//	/**
//	 * get list
//	 */
//	public GenericListBean<T> list(T field) {
//		return getList(field);
//	}

	public Set<T> getValKeySet() {
		return values.keySet();
	}

//	public Set<T> getListKeySet() {
//		return lists.keySet();
//	}
}
