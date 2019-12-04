/*
 * DummyId.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing.flex;

/**
 * Created by rick on 2015-05-05.
 */
public class DummyId {
    private final int value;

    public DummyId(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ParameterParser<DummyId> getParameterParser() {
        return new ParameterParser<DummyId>() {
            @Override
            public DummyId parse(String value) {
                try {
                    return new DummyId(Integer.valueOf(value));
                }
                catch (NumberFormatException e) {
                    return null;
                }
            }
        };
    }
}
