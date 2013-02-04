/*
 * LocalizationContext.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.i18n;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides access to the resource bundle and the locale that actually matched.
 *
 * @author pmairif
 */
public class LocalizationContext {
	
	/**
	 * Logger for this class
	 */
	private final Log log = LogFactory.getLog(LocalizationContext.class);

	/**
	 * Locale automatically chosen
	 */
	private Locale locale = null;
	
	private ResourceBundle resourceBundle = null;

	private final List<Locale> locales;
	
	private final String bundleName;

	/**
	 * @param bundleName
	 * 		the name of your localization bundle
	 * @param locale
	 * 		preferred locale
	 */
	public LocalizationContext(String bundleName, Locale locale) {
		this.bundleName = bundleName;
		this.locales = null;
		this.locale = locale;
	}
	
	/**
	 * @param bundleName
	 * 		the name of your localization bundle
	 * @param locales
	 * 		the locales accepted by the user (usually from the 'Accept-Language' header, available via HttpServletRequest.getLocales())
	 * @param locale
	 * 		preferred locale
	 */
	public LocalizationContext(String bundleName, List<Locale> locales, Locale locale) {
		this.bundleName = bundleName;
		this.locales = locales;
		this.locale = locale;
	}
	
	/**
	 * @param bundleName
	 * 		the name of your localization bundle
	 * @param locales
	 * 		the locales accepted by the user (usually from the 'Accept-Language' header, available via HttpServletRequest.getLocales())
	 * @param locale
	 * 		preferred locale
	 */
	public LocalizationContext(String bundleName, Enumeration<Locale> locales, Locale locale) {
		this.bundleName = bundleName;
		this.locale = locale;
		
		this.locales = new Vector<Locale>();
		while (locales.hasMoreElements())
			this.locales.add(locales.nextElement());
	}

	public Locale getLocale() {
		return locale;
	}
	
	/**
	 * used by the fallback handler to set the locale
	 * @param locale the locale to set
	 */
	protected void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * @return the resourceBundle
	 */
	public synchronized ResourceBundle getResourceBundle() {
		if (null == resourceBundle) {
			this.resourceBundle = ResourceBundle.getBundle(bundleName, locale, new ResourceBundleControl());
		}
		return resourceBundle;
	}
	
	class ResourceBundleControl extends ResourceBundle.Control {
		/* (non-Javadoc)
		 * @see java.util.ResourceBundle.Control#getFallbackLocale(java.lang.String, java.util.Locale)
		 */
		@Override
		public Locale getFallbackLocale(String baseName, Locale locale) {
			Locale fallback = null;
			
			if (locales != null) {
				Iterator<Locale> it = locales.iterator();
				while (it.hasNext()) {
					Locale next = it.next();
					if (next.equals(locale) && it.hasNext()) {
						fallback = it.next();
						break;
					}
				}
			}

			if (null != fallback && log.isDebugEnabled()) {
				log.debug("getFallbackLocale() - checking fallback locale "+fallback);
			}
			
			setLocale(fallback);
			
			return fallback;
		}
	}

}
