/*
 * TextFormat.java (weberknecht)
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 2009-03-22
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * generate HTML out of formatted plain text
 * 
 * @author pmairif
 */
public abstract class TextFormat {

	private static final Pattern lineBreakPattern = Pattern.compile("\n");

	/**
	 * convert line breaks etc.
	 * 
	 * @param htmlEscapedText
	 * 		text that is already HTML escaped
	 */
	public static String getTextHtmlFormatted(String htmlEscapedText) {
		//replace line breaks
		Matcher m = lineBreakPattern.matcher(htmlEscapedText);
		return m.replaceAll("<br />");
	}
}
