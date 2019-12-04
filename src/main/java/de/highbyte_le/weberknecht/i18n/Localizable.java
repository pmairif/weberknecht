/*
 * Localizable.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import de.highbyte_le.weberknecht.request.routing.LocalePrefixRouter;

/**
 * Actions implementing this interface will get the requested locale via the {@link LocalePrefixRouter} or a similar router.
 * 
 * The locale, that will be set is not the locale extracted from the 'Accept-Language' header, it is extracted from the URL.
 * It's up to the action to decide which one to use. A good practice is to use that from the URL, if present.
 * If not, choose a locale based on the header via the mechanism from {@link ResourceBundle} and redirect to an URL containing
 * the chosen language. {@link LocaleMatcher} provides some helpful methods.
 *
 * @author pmairif
 */
public interface Localizable {
	/**
	 * set the locale extracted from the requested URL, if present.
	 * 
	 * @param requestedLocale	the locale or null
	 */
	public void setRequestedLocale(Locale requestedLocale);
}
