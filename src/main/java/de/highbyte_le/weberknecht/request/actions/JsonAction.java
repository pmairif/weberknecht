/*
 * JsonAction.java
 *
 * Copyright 2009-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.actions;

import de.highbyte_le.weberknecht.request.view.JsonView;

/**
 * webapp actions producing JSON data (see http://www.json.org/)
 * 
 * @author pmairif
 */
public interface JsonAction extends ExecutableAction, JsonView {
}
