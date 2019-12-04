/*
 * FeedAction.java
 *
 * Copyright 2009-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.actions;

import com.github.pmairif.weberknecht.request.view.FeedView;

/**
 * webapp actions producing feeds (RSS/Atom)
 * 
 * @author pmairif
 */
public interface FeedAction extends ExecutableAction, FeedView {
}
