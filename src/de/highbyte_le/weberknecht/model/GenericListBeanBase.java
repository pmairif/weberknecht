/*
 * GenericListBeanBase.java (weberknecht)
 * 
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=UTF-8, tabstob=4
 */
package de.highbyte_le.weberknecht.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * hold list data for web pages.
 * 
 * <p>This could be a replacement for some of your hand made page beans.</p>  
 * 
 * @see GenericBean
 * @author pmairif
 */
public class GenericListBeanBase <T, G extends GenericBean<T>> implements Serializable {

	private List<G> list = null;
	
	private Iterator<G> iterator = null;
	
	private G current = null;

	private static final long serialVersionUID = -5511365146214652801L;

	public GenericListBeanBase() {
		list = new Vector<G>();
	}

	public GenericListBeanBase(List<G> list) {
		this.list = list;
	}

	public void populate(List<G> _list) {
		this.list = _list;
	}

	public boolean add(G e) {
		return this.list.add(e);
	}
	
	public void reset() {
		iterator = list.iterator();
		current = null;
	}

	public boolean next() {
		if (null == iterator)
			iterator = list.iterator();
		
		if (iterator.hasNext()) {
			current = iterator.next();
			return true;
		}
		return false;
	}
	
	/**
	 * get the current element
	 */
	public G getCurrent() {
		checkCurrent();
		return current;
	}
	
	/**
	 * checks, if current value is present
	 */
	public boolean hasCurrentValue(T field) {
		checkCurrent();
		return current.hasValue(field);
	}

	/**
	 * get current value unmodified
	 */
	public String getCurrentValue(T field) {
		checkCurrent();
		return current.getValue(field);
	}

	/**
	 * get current value html escaped
	 */
	public String getCurrentValue_htmlEscaped(T field) {
		checkCurrent();
		return current.getValue_htmlEscaped(field);
	}

	public Set<T> getCurrentValKeySet() {
		checkCurrent();
		return current.getValKeySet();
	}

	public boolean hasEntries() {
		return (list != null && list.size() > 0);
	}

	/**
	 * checks, if current value is present
	 */
	public boolean hasCurVal(T field) {
		return hasCurrentValue(field);
	}

	/**
	 * get current value unmodified
	 */
	public String curVal(T field) {
		return getCurrentValue(field);
	}

	/**
	 * get current value html escaped
	 */
	public String curValHtmlEsc(T field) {
		return getCurrentValue_htmlEscaped(field);
	}

	private void checkCurrent() {
		if (null == current)
			throw new RuntimeException("current is not set");
	}
}
