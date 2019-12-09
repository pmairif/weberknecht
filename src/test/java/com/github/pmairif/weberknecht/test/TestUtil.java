/*
 * TestUtil.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.pmairif.weberknecht.conf.WeberknechtConf;
import com.github.pmairif.weberknecht.conf.ConfigurationException;

/**
 * @author pmairif
 */
public class TestUtil {
	public static WeberknechtConf readConfig(String filename) throws IOException, ConfigurationException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(filename));
			return WeberknechtConf.readConfig(in);
		}
		finally {
			if (in != null)
				in.close();
		}
	}
}
