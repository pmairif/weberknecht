/*
 * GenericListBean.java (weberknecht)
 * 
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * charset=UTF-8, tabstob=4
 */
package de.highbyte_le.weberknecht.model;

import java.util.List;

/**
 * hold list data for web pages.
 * 
 * <p>This could be a replacement for some of your hand made page beans.</p>  
 * 
 * @see GenericBean
 * @author pmairif
 */
public class GenericListBean<T> extends GenericListBeanBase<T, GenericBean<T>> {

	private static final long serialVersionUID = 8058334685517115487L;

	public GenericListBean() {
		super();
	}

	public GenericListBean(List<GenericBean<T>> list) {
		super(list);
	}
}
