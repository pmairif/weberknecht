/*
 * Rpx.java
 *
 * Copyright 2009 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.rpx;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * handle RPX responses
 * 
 * @author pmairif
 */
public class Rpx {
	/**
	 * Logger for this class
	 */
	private final Log logger = LogFactory.getLog(Rpx.class);

	private String apiKey;
    
    private String baseUrl;

    public Rpx(String apiKey) {
        this.baseUrl = "https://rpxnow.com";
        this.apiKey = apiKey;
    }

    public String getApiKey() { return apiKey; }
    public String getBaseUrl() { return baseUrl; }

    @SuppressWarnings("unchecked")
	public RpxAuthInfo authInfo(String token) throws RpxException {
        Map<String, String> query = new HashMap<String, String>();
        query.put("token", token);
        Element element = apiCall("auth_info", query);
        
        RpxAuthInfo rai = new RpxAuthInfo();
        
        Element profile = element.getChild("profile");
        for (Element child: (List<Element>)profile.getChildren()) {
        	rai.put(child.getName(), child.getTextNormalize());
        }
        
        return rai;
    }

//    public List<String> mappings(String primaryKey) throws RpxException {
//        Map<String, String> query = new HashMap<String, String>();
//        query.put("primaryKey", primaryKey);
//        Element rsp = apiCall("mappings", query);
//        Element oids = (Element)rsp.getFirstChild();
//
//        List<String> result = new ArrayList<String>();
//
//        NodeList nl = oids.getChildNodes();
//        for (int i = 0; i < nl.getLength(); i++) {
//            Element e = (Element)nl.item(i);
//            result.add(e.getTextContent());
//        }
//
//        return result;
//    }

    @SuppressWarnings("unchecked")
	public List<String> mappings(String primaryKey) throws RpxException {
        Map<String, String> query = new HashMap<String, String>();
        query.put("primaryKey", primaryKey);
        Element rsp = apiCall("mappings", query);
        Element oids = (Element)rsp.getChildren().get(0);

        List<String> result = new ArrayList<String>();

        for (Element e: (List<Element>)oids.getChildren()) {
        	result.add(e.getText());
        }

        return result;
    }

    public void map(String identifier, String primaryKey) throws RpxException {
        Map<String, String> query = new HashMap<String, String>();
        query.put("identifier", identifier);
        query.put("primaryKey", primaryKey);
        apiCall("map", query);
    }

    public void unmap(String identifier, String primaryKey) throws RpxException {
        Map<String, String> query = new HashMap<String, String>();
        query.put("identifier", identifier);
        query.put("primaryKey", primaryKey);
        apiCall("unmap", query);
    }

//    private Element apiCall(String methodName, Map<String, String> partialQuery) throws RpxException {
//        Map<String, String> query = new HashMap<String, String>(partialQuery);
//        query.put("format", "xml");
//        query.put("apiKey", apiKey);
//
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, String> e: query.entrySet()) {
//            if (sb.length() > 0) sb.append('&');
//
//            try {
//                sb.append(URLEncoder.encode(e.getKey().toString(), "UTF-8"));
//                sb.append('=');
//                sb.append(URLEncoder.encode(e.getValue().toString(), "UTF-8"));
//            }
//            catch (UnsupportedEncodingException ex) {
//            	logger.error("unexpected encoding error: "+ex.getMessage(), ex);
//            }
//        }
//        String data = sb.toString();
//
//        try {
//	        URL url = new URL(baseUrl + "/api/v2/" + methodName);
//	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//	        conn.setRequestMethod("POST");
//	        conn.setDoOutput(true);
//	
//	        conn.connect();
//	        OutputStreamWriter osw = new OutputStreamWriter(
//	            conn.getOutputStream(), "UTF-8");
//	        osw.write(data);
//	        osw.close();
//	
//	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//	        dbf.setIgnoringElementContentWhitespace(true);
//	        DocumentBuilder db = dbf.newDocumentBuilder();
//	        Document doc = db.parse(conn.getInputStream());
//	        Element response = (Element)doc.getFirstChild();
//
//	        if (!response.getAttribute("stat").equals("ok")) {
//	            throw new RpxException("Unexpected API error");
//	        }
//
//	        return response;
//        }
//        catch (IOException e) {
//        	throw new RpxException("i/o exception: "+e.getMessage(), e);
//        }
//		catch (SAXException e) {
//        	throw new RpxException("sax exception: "+e.getMessage(), e);
//		}
//		catch (ParserConfigurationException e) {
//        	throw new RpxException("parser configuration exception: "+e.getMessage(), e);
//		}
//    }

    private Element apiCall(String methodName, Map<String, String> partialQuery) throws RpxException {
        Map<String, String> query = new HashMap<String, String>(partialQuery);
        query.put("format", "xml");
        query.put("apiKey", apiKey);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e: query.entrySet()) {
            if (sb.length() > 0) sb.append('&');

            try {
                sb.append(URLEncoder.encode(e.getKey().toString(), "UTF-8"));
                sb.append('=');
                sb.append(URLEncoder.encode(e.getValue().toString(), "UTF-8"));
            }
            catch (UnsupportedEncodingException ex) {
            	logger.error("unexpected encoding error: "+ex.getMessage(), ex);
            }
        }
        String data = sb.toString();

        try {
	        URL url = new URL(baseUrl + "/api/v2/" + methodName);
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	
	        conn.connect();
	        OutputStreamWriter osw = new OutputStreamWriter(
	            conn.getOutputStream(), "UTF-8");
	        osw.write(data);
	        osw.close();
	
	        Document doc = new SAXBuilder().build(conn.getInputStream());
	        Element response = doc.getRootElement();

	        if (!response.getAttributeValue("stat").equals("ok")) {
	            throw new RpxException("Unexpected API error");
	        }

	        return response;
        }
        catch (IOException e) {
        	throw new RpxException("i/o exception: "+e.getMessage(), e);
        }
		catch (JDOMException e) {
        	throw new RpxException("jdom exception: "+e.getMessage(), e);
		}
    }
}
