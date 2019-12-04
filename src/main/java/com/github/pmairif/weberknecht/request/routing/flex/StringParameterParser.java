/*
 * StringParameterParser.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing.flex;

/**
 * Created by rick on 2015-05-05.
 */
public class StringParameterParser implements ParameterParser<String> {
    @Override
    public String parse(String value) {
        return value;
    }
}
