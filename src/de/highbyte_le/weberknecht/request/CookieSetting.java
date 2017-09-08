/*
 * CookieSetting.java
 *
 * tabstop=4, charset=utf8
 */
package de.highbyte_le.weberknecht.request;

import java.util.Collection;

import javax.servlet.http.Cookie;

/**
 * Implement this interface, if you want to set cookies in actions.
 * @author pmairif
 */
public interface CookieSetting {
	/**
	 * collection of cookies, that are send with the response.
	 */
	Collection<Cookie> getCookies();
}
