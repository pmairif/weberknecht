/*
 * WeberknechtConfTest.java (weberknecht)
 *
 * Copyright 2010 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 24.03.2010
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.conf;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

/**
 * Testing {@link WeberknechtConf}
 * 
 * @author pmairif
 */
public class WeberknechtConfTest {

	//TODO test areas
	
	/**
	 * Test method for {@link de.highbyte_le.weberknecht.conf.WeberknechtConf#readConfig(javax.servlet.ServletContext)}.
	 */
	@Test
	public void testReadConfig1() throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File("test-data/weberknecht-1.xml"));
			WeberknechtConf conf = WeberknechtConf.readConfig(in);
			
			Map<String, String> expectedActionClassMap = new HashMap<String, String>();
			expectedActionClassMap.put("foo", "de.highbyte_le.weberknecht.FooAction");
			expectedActionClassMap.put("bar", "de.highbyte_le.weberknecht.BarAction");
			assertEquals(expectedActionClassMap, conf.getActionClassMap());

			List<String> expectedPreProcessorClasses = new Vector<String>();
			expectedPreProcessorClasses.add("de.highbyte_le.weberknecht.PreProcessor1");
			expectedPreProcessorClasses.add("de.highbyte_le.weberknecht.PreProcessor2");
			assertEquals(expectedPreProcessorClasses, conf.getPreProcessorClasses());

			List<String> expectedPostProcessorClasses = new Vector<String>();
			expectedPostProcessorClasses.add("de.highbyte_le.weberknecht.PostProcessor1");
			expectedPostProcessorClasses.add("de.highbyte_le.weberknecht.PostProcessor2");
			assertEquals(expectedPostProcessorClasses, conf.getPostProcessorClasses());

		}
		finally {
			if (in != null)
				in.close();
		}
	}
}
