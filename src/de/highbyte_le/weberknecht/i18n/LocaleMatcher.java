/*
 * LocaleMatcher.java (weberknecht)
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
 * locale matching
 *
 * @author pmairif
 */
public class LocaleMatcher {
	
//	/**
//	 * Logger for this class
//	 */
//	private final Log log = LogFactory.getLog(LocaleMatcher.class);

	private ResourceBundle resourceBundle = null;

	private final List<Locale> locales;
	
	private final String bundleName;

	private Locale defaultLocale = Locale.getDefault();
	
	/**
	 * @param bundleName
	 * 		the name of your localization bundle
	 * @param locales
	 * 		the locales accepted by the user (usually from the 'Accept-Language' header, available via HttpServletRequest.getLocales())
	 */
	public LocaleMatcher(String bundleName, List<Locale> locales) {
		this.bundleName = bundleName;
		this.locales = locales;
	}
	
	/**
	 * @param bundleName
	 * 		the name of your localization bundle
	 * @param locales
	 * 		the locales accepted by the user (usually from the 'Accept-Language' header, available via HttpServletRequest.getLocales())
	 */
	public LocaleMatcher(String bundleName, Enumeration<Locale> locales) {
		this.bundleName = bundleName;
		
		this.locales = new Vector<Locale>();
		while (locales.hasMoreElements())
			this.locales.add(locales.nextElement());
	}

	public Locale findLocale() {
		return findResourceBundle().getLocale();
	}
	
	/**
	 * @return the resourceBundle
	 */
	public synchronized ResourceBundle findResourceBundle() {
		if (null == resourceBundle) {
			if (locales.size() > 0) {
				Locale firstLocale = locales.get(0);
			
				this.resourceBundle = ResourceBundle.getBundle(bundleName, firstLocale, new ResourceBundleControl());
			}
			
			if (null == resourceBundle || "".equals(this.resourceBundle.getLocale().toString()))
				this.resourceBundle = ResourceBundle.getBundle(bundleName, defaultLocale, new ResourceBundleControl());
		}
		
		return resourceBundle;
	}
	
	/**
	 * @param defaultLocale the defaultLocale to set
	 */
	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}
	
	/**
	 * @return the defaultLocale
	 */
	public Locale getDefaultLocale() {
		return defaultLocale;
	}
	
	/**
	 * @return the locales
	 */
	protected List<Locale> getLocales() {
		return locales;
	}

	class ResourceBundleControl extends ResourceBundle.Control {
		private final Log log = LogFactory.getLog(ResourceBundleControl.class);

		/* (non-Javadoc)
		 * @see java.util.ResourceBundle.Control#getFallbackLocale(java.lang.String, java.util.Locale)
		 */
		@Override
		public Locale getFallbackLocale(String baseName, Locale locale) {
			Locale fallback = null;
			
			List<Locale> locales = getLocales();
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
			
			return fallback;
		}
	}
}
