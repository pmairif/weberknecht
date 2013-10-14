/*
 * RequestWrapper.java
 * 
 * Copyright 2007-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Wraps the HttpServletRequest and handles charset encoding of URL encoded strings correctly. 
 * 
 * @author pmairif
 */
@SuppressWarnings("nls")
public class RequestWrapper {
    private Map<String, String[]> parameters = new HashMap<String, String[]>();

	private static final Log logger = LogFactory.getLog(RequestWrapper.class);

	private RequestWrapper() {
    	//
    }
    
    /**
     * read parameters from query string (HTTP GET)
     */
    public static RequestWrapper createFromQueryString(HttpServletRequest request) throws UnsupportedEncodingException {
    	return createFromQueryString(request, "UTF-8");
    }
    
    /**
     * read parameters from query string (HTTP GET)
     */
    public static RequestWrapper createFromQueryString(HttpServletRequest request, String urlEncoding) throws UnsupportedEncodingException {
    	RequestWrapper wrapper = new RequestWrapper();
    	
        String query = request.getQueryString();
        if (query != null) {						//use URL parameters from query string
            int startIndex = 0;
            int i;
            do {
	            i = query.indexOf("&", startIndex);
	            String nameValue;
	            if (i != -1) {
	                nameValue = query.substring(startIndex, i);
	            }
	            else {
	                nameValue = query.substring(startIndex, query.length());
	            }
	            
	            int j = nameValue.indexOf("=");
	            if (j != -1) {
	                String name = URLDecoder.decode(nameValue.substring(0, j), urlEncoding);
	                String value = URLDecoder.decode(nameValue.substring(j+1, nameValue.length()), urlEncoding);
	                wrapper.addParameter(name, value);
	                logger.debug("createFromQueryString(HttpServletRequest, String) - found parameter: "+name+"="+value);
	            }
	            
	            startIndex = i+1;
            } while (i != -1);
        }
        
        return wrapper;
    }

    /**
     * read URL encoded parameters (enctype="application/x-www-form-urlencoded") from HTTP method body (HTTP POST)
     * 
     * <p>the default reader is used to read the method body.
     * The specified encoding is only used to decode the URL parameters, not to read the method body.
     * </p>
     * 
     * @param request
     * @param urlEncoding
     * 		the character set encoding used to decode the URL encoded parameters.
     */
    public static RequestWrapper createFromUrlEncodedContent(HttpServletRequest request, String urlEncoding) throws IOException {
    	RequestWrapper wrapper = new RequestWrapper();
    	int contentLength = request.getContentLength();
    	if (logger.isDebugEnabled())
    		logger.debug("content length is "+contentLength);
    	
        if (contentLength > 0) {
        	String query = extractQuery(request);
        	
        	if (logger.isDebugEnabled())
        		logger.debug("query is '"+query+"'");
        	
            int startIndex = 0;
            int i;
            do {
	            i = query.indexOf("&", startIndex);
	            String nameValue;
	            if (i != -1) {
	                nameValue = query.substring(startIndex, i);
	            }
	            else {
	                nameValue = query.substring(startIndex, query.length());
	            }
	            
	            int j = nameValue.indexOf("=");
	            if (j != -1) {
	                String name = URLDecoder.decode(nameValue.substring(0, j), urlEncoding);
	                String value = URLDecoder.decode(nameValue.substring(j+1, nameValue.length()), urlEncoding);
	                wrapper.addParameter(name, value);
	                logger.debug("createFromUrlEncodedContent(HttpServletRequest, String) - found parameter: "+name+"="+value);
	            }
	            
	            startIndex = i+1;
            } while (i != -1);
        }
        
        return wrapper;
    }

	private static String extractQuery(HttpServletRequest request) throws IOException {
		StringBuilder b = new StringBuilder();
		
		BufferedReader reader = request.getReader();
		try {
			String line;
			while ((line=reader.readLine()) != null) {
				b.append(line);
				logger.debug(line);
			}
		}
		finally {
			reader.close();
		}
	
		return b.toString();
	}

    private void addParameter(String name, String value) {
    	if (this.parameters.containsKey(name)) {		//append value to list of existing values
    		String[] values = this.parameters.get(name);
    		int l = values.length;
    		
    		String[] newValues = new String[l+1];
    		for (int i=0; i<l; i++) {
    			newValues[i] = values[i];
    		}
    		newValues[l] = value;
    		
    		this.parameters.put(name, newValues);
    	}
    	else {											//store new value
    		this.parameters.put(name, new String[]{value});
    	}
    }
    
    /**
     * get the value of the parameter.
     * 
     * <p>If there is more than one value, the first one is returned. </p>
     * 
     * @param name
     * @return the first value or null, if there is no value
     */
    public String getParameter(String name) {
    	String[] values = this.parameters.get(name);
    	if (values != null && values.length > 0 && values[0] != null)
    		return values[0];

    	return null;
    }
    
    /**
     * get all values as string array.
     * @param name
     * @return the values as string array or null, if the name is unknown
     */
    public String[] getParameters(String name) {
    	return this.parameters.get(name);
    }

    /**
	 * parses an URL parameter as decimal integer value
	 * @param name	name of the variable
	 * @return value the value as integer or 0, if it could not be parsed
	 */
	public int getParameterAsInt(String name) {
        return parseInt( getParameter(name) );
	}

	public int[] getParametersAsInt(String name) {
		String[] params = getParameters(name);
		if (null == params)
			return null;
		
		int[] ret = new int[params.length];
		
		int i=0;
		for (String p: params) {
			ret[i] = parseInt(p);
		    i++;
		}
		
		return ret;
	}

	int parseInt(String value) {
	    int ret = 0;
	    
	    try {
	        if (value != null)
	            ret = Integer.parseInt(value);
	    }
	    catch (NumberFormatException e) {
	        logger.warn("number format exception while parsing '"+value+"' as int");
	    }
	    
	    return ret;
	}

	/**
	 * parses an URL parameter as decimal float value
	 * @param name	name of the variable
	 * @return value the value as float or 0, if it could not be parsed
	 */
	public float getParameterAsFloat(String name) {
        return parseFloat( getParameter(name) );
	}

	public float[] getParametersAsFloat(String name) {
		String[] params = getParameters(name);
		if (null == params)
			return null;

		float[] ret = new float[params.length];
		
		int i=0;
		for (String p: params) {
			ret[i] = parseFloat(p);
		    i++;
		}
		
		return ret;
	}
	
	float parseFloat(String value) {
	    try {
	        if (value != null)
	            return Float.parseFloat(value);
	    }
	    catch (NumberFormatException e) {
	        logger.warn("number format exception while parsing '"+value+"' as float");
	    }

	    return 0f;
	}

	public boolean getParameterAsBoolean(String name) {
		return parseBoolean( getParameter(name) );
	}

	public boolean[] getParametersAsBoolean(String name) {
		String[] params = getParameters(name);
		if (null == params)
			return null;

		boolean[] ret = new boolean[params.length];
		
		int i=0;
		for (String p: params) {
			ret[i] = parseBoolean(p);
		    i++;
		}
		
		return ret;
	}

	boolean parseBoolean(String value) {
		boolean ret = false;
		
		if (value != null)
			ret = (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") ||  value.equalsIgnoreCase("on"));
			
		return ret;
	}

	/**
	 * parse ISO-Date
	 * @param name the name of the parameter
	 * @return new date object or null
	 */
	public Date getParameterAsDate(String name) {
		return parseDate( getParameter(name) );
	}

	public Date[] getParametersAsDate(String name) {
		String[] params = getParameters(name);
		if (null == params)
			return null;

		Date[] ret = new Date[params.length];
		
		int i=0;
		for (String p: params) {
			ret[i] = parseDate(p);
		    i++;
		}
		
		return ret;
	}

	Date parseDate(String value) {
		Date date = null;
		
		if (value != null && value.length() > 0) {
			try {
				SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				date = isoDateFormat.parse(value);
			}
			catch (ParseException e) {
				logger.warn("getParameterAsDate() - exception while parsing date: "+e.getMessage());
			}
		}
		
		return date;
	}

	public <T extends Enum<T>> T getParameterAsEnum(String name, Class<T> enumType) {
		return getParameterAsEnum(name, enumType, null);
	}
    
	public <T extends Enum<T>> T getParameterAsEnum(String name, Class<T> enumType, T defaultValue) {
		T retVal = defaultValue;
		String val = getParameter(name);
		try {
			if (val != null) {
				retVal = Enum.valueOf(enumType, val);
			}
		}
		catch (IllegalArgumentException e) {
			logger.debug("getParameterAsEnum() - " +
					"invalid enum value '"+val+"' for parameter "+name+" and type "+enumType.getCanonicalName());
		}
		
		return retVal;
	}
	/**
	 * @param name
	 * @param allowed
	 * @return one of the allowed strings or the default string
	 */
	public String getParameterAsEnumString(String name, Set<String> allowed, String defaultVal) {
	    String ret = defaultVal;
	    
        String val = getParameter(name);
	    if (val != null && allowed.contains(val))
	        ret = val;
	    else if (val != null && val.length()>0)
	        logger.warn("getParameterAsEnumString(String, Set<String>, String) - " +
	        		"the url parameter '"+name+"' contains an unallowed value: '"+val+"'");
	    
	    return ret;
	}

	/**
	 * @param name
	 * @param allowed
	 * @return one of the allowed strings or the empty string
	 */
	public String getParameterAsEnumString(String name, Set<String> allowed) {
	    return getParameterAsEnumString(name, allowed, "");
	}

	
	/**
	 * @param name
	 * @param allowed
	 * @param defaultVal	default value used if none of the allowed strings is available
	 * @return one of the allowed strings or the default value
	 */
	public String getParameterAsEnumString(String name, String[] allowed, String defaultVal) {
	    String ret = defaultVal;
	    boolean found = false;

        String val = getParameter(name);
	    if (val != null) {
		    for (int i=0; i<allowed.length && !found; i++) {
		        if (allowed[i].equals(val)) {
		            ret = val;
		            found = true;
		        }
		    }	        
	    }

	    if (!found && val!=null && val.length()>0)
	        logger.warn("getParameterAsEnumString(String, String[], String) - " +
	        		"the url parameter '"+name+"' contains an unallowed value: '"+val+"'");
	    
	    return ret;
	}

	/**
	 * @param name
	 * @param allowed
	 * @return one of the allowed strings or the empty string
	 */
	public String getParameterAsEnumString(String name, String[] allowed) {
	    return getParameterAsEnumString(name, allowed, "");
	}

	public boolean containsParameter(String name) {
	    return this.parameters.containsKey(name);
	}
}
