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
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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

	private final Enumeration<Locale> locales;
	
	private final String bundleName;

	/**
	 * 
	 * @param bundleName
	 * 		the name of your localization bundle
	 * @param locales
	 * 		the locales accepted by the user (usually from the 'Accept-Language' header, available via HttpServletRequest.getLocales())
	 */
	public LocalizationContext(String bundleName, Enumeration<Locale> locales) {
		this.bundleName = bundleName;
		this.locales = locales;
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
	 * get localized string
	 */
	public String getString(String key) {
		try {
			return getResourceBundle().getString(key);
		}
		catch (MissingResourceException e) {
			log.error("l10n key not found: "+key);
			return '!' + key + '!';
		}
	}
	
	/**
	 * @return the resourceBundle
	 */
	protected synchronized ResourceBundle getResourceBundle() {
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
			
			while (locales != null && locales.hasMoreElements()) {
				Locale next = locales.nextElement();
				if (next.equals(locale) && locales.hasMoreElements()) {
					fallback = locales.nextElement();
					break;
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
