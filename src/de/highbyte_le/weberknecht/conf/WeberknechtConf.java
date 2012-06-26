/*
 * WeberknechtConf.java (weberknecht)
 *
 * Copyright 2009-2011 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 2009-09-14
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * weberknecht configuration object
 * @author pmairif
 */
public class WeberknechtConf {
	/**
	 * Mapping of areas to actions to classes.
	 * Default area is represented by the empty string.
	 */
	private Map<String, Map<String, String>> areaActionClassMap = new HashMap<String, Map<String, String>>();

	private List<String> preProcessorClasses = new Vector<String>();

	private List<String> postProcessorClasses = new Vector<String>();

	/**
	 * context names of used databases
	 */
	private Set<String> dbs = new HashSet<String>();
	
	private String routerClass = null;
	
	/**
	 * Mapping of suffixes to ActionViewProcessor classes.
	 */
	private Map<String, String> actionProcessorSuffixMap = new HashMap<String, String>();
	
	/**
	 * Logger for this class
	 */
	private final static Log log = LogFactory.getLog(WeberknechtConf.class);

	private WeberknechtConf() {
		//
	}
	
	/**
	 * reads weberknecht.xml or actions.xml if there is no weberknecht.xml
	 * and creates new config instance
	 */
	public static WeberknechtConf readConfig(ServletContext servletContext) {
		WeberknechtConf conf = null;
		try {

			//read available actions from weberknecht.xml
			InputStream in = null;
			try {
				in = servletContext.getResourceAsStream("/WEB-INF/weberknecht.xml");
				if (in != null)
					conf = readConfig(in);
				else {
					in = servletContext.getResourceAsStream("/WEB-INF/actions.xml");
					if (in != null)
						conf = readActions(in);
				}
					
			}
			finally {
				if (in != null) in.close();
			}
			
		}
		catch (IOException e) {
			log.error("readConfig() - IOException: "+e.getMessage(), e);	//$NON-NLS-1$
		}

		if (null == conf)
			return new WeberknechtConf();
		return conf;
	}
	
	/**
	 * reads weberknecht.xml from input stream and creates new config instance
	 */
	static WeberknechtConf readConfig(InputStream in) {
		WeberknechtConf conf = new WeberknechtConf();
		
		Document doc = null;
		try {
			doc = new SAXBuilder().build(in);
			Element rootElement = doc.getRootElement();
			
			readDbs(conf, rootElement);
			readPreProcessors(conf, rootElement);
			readPostProcessors(conf, rootElement);
			readRouter(conf, rootElement);
			readActionViewProcessors(conf, rootElement);
			readActions(conf, rootElement);
		}
		catch (JDOMException e1) {
			log.error("readConfig() - JDOMException: "+e1.getMessage(), e1);	//$NON-NLS-1$
		}
		catch (IOException e1) {
			log.error("readConfig() - IOException: "+e1.getMessage(), e1);	//$NON-NLS-1$
		}

		return conf;
	}

	/**
	 * @param conf
	 * @param rootElement
	 */
	@SuppressWarnings("unchecked")
	protected static void readActions(WeberknechtConf conf, Element rootElement) {
		List<Element> actionsElements = rootElement.getChildren("actions");
		if (actionsElements != null) {
			for (Element e: actionsElements) {
				String area = e.getAttributeValue("area");
				if (area == null)
					area = "";
				Map<String, String> actionClassMap = conf.getActionClassMap(area);
				
				List<Element> actionElements = e.getChildren("action");
				if (null == actionElements) {
					log.error("readConfig() - There are no actions configured for area \""+area+"\"");
				}
				else {
					for (Element e1: actionElements) {
						String name = e1.getAttributeValue("name");
						String className = e1.getAttributeValue("class");
						
						if (name != null && className != null)
							actionClassMap.put(name, className);
						else
							log.error("readConfig() - invalid action configuration: name="+name+", class="+className);
					}
				}
			}
		}
		else {
			log.error("readConfig() - There are no actions configured!");
		}
	}

	/**
	 * @param conf
	 * @param rootElement
	 */
	protected static void readRouter(WeberknechtConf conf, Element rootElement) {
		Element routerElement = rootElement.getChild("router");
		if (routerElement != null) {
			conf.routerClass = routerElement.getAttributeValue("class");
		}
	}

	/**
	 * @param conf
	 * @param rootElement
	 */
	@SuppressWarnings("unchecked")
	protected static void readPostProcessors(WeberknechtConf conf, Element rootElement) {
		Element postProcessorsElement = rootElement.getChild("post-processors");
		if (postProcessorsElement != null) {
			List<Element> postProcessorElements = postProcessorsElement.getChildren("post-processor");
			if (postProcessorElements != null) {
				for (Element e: postProcessorElements) {
					String className = e.getAttributeValue("class");
					conf.postProcessorClasses.add(className);
				}
			}
		}
	}

	/**
	 * @param conf
	 * @param rootElement
	 */
	@SuppressWarnings("unchecked")
	protected static void readPreProcessors(WeberknechtConf conf, Element rootElement) {
		Element preProcessorsElement = rootElement.getChild("pre-processors");
		if (preProcessorsElement != null) {
			List<Element> preProcessorElements = preProcessorsElement.getChildren("pre-processor");
			if (preProcessorElements != null) {
				for (Element e: preProcessorElements) {
					String className = e.getAttributeValue("class");
					conf.preProcessorClasses.add(className);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected static void readDbs(WeberknechtConf conf, Element rootElement) {
		Element dbsElement = rootElement.getChild("dbs");
		if (dbsElement != null) {
			List<Element> dbElements = dbsElement.getChildren("db");
			if (dbElements != null) {
				for (Element e: dbElements) {
					conf.dbs.add(e.getTextNormalize());
				}
			}
			else {
				log.debug("readConfig() - There is no additional db configured");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected static void readActionViewProcessors(WeberknechtConf conf, Element rootElement) {
		Element processorsElement = rootElement.getChild("action-view-processors");
		if (processorsElement != null) {
			List<Element> processorElements = processorsElement.getChildren("action-view-processor");
			if (processorElements != null) {
				for (Element e: processorElements) {
					String suffix = e.getAttributeValue("suffix");
					String className = e.getAttributeValue("class");
					conf.actionProcessorSuffixMap.put(suffix, className);
				}
			}
			else {
				log.debug("readConfig() - There is no additional action view processor configured");
			}
		}
	}
	
	/**
	 * read available actions from actions.xml
	 */
	@SuppressWarnings("unchecked")
	static WeberknechtConf readActions(InputStream in) {
		WeberknechtConf conf = new WeberknechtConf();
		
		List<Element> actionElements = null;
		
		Document doc = null;
		try {
			doc = new SAXBuilder().build(in);
			actionElements = doc.getRootElement().getChildren("action");

			if (actionElements != null) {
				Map<String, String> actionClassMap = conf.getActionClassMap("");	//actions for default area

				for (Element e: actionElements) {
					String name = e.getAttributeValue("name");
					String className = e.getAttributeValue("class");
					
					if (name != null && className != null)
						actionClassMap.put(name, className);
					else
						log.error("readActions() - invalid action configuration: name="+name+", class="+className);
				}
			}
			else {
				log.error("readActions() - There are no actions configured!");
			}
		}
		catch (JDOMException e1) {
			log.error("readActions() - JDOMException: "+e1.getMessage(), e1);	//$NON-NLS-1$
		}
		catch (IOException e1) {
			log.error("readActions() - IOException: "+e1.getMessage(), e1);	//$NON-NLS-1$
		}
		
		return conf;
	}

	/**
	 * get the action map for the default area
	 */
	public Map<String, String> getActionClassMap() {
		return getActionClassMap("");
	}
	
	/**
	 * get the action map for the area
	 */
	public synchronized Map<String, String> getActionClassMap(String area) {
		String key = area != null ? area : "";
		Map<String, String> ret = areaActionClassMap.get(key);
		
		if (null == ret) {
			ret = new HashMap<String, String>();
			areaActionClassMap.put(key, ret);
		}
		
		return ret;
	}
	
	/**
	 * @return the areaActionClassMap
	 */
	public Map<String, Map<String, String>> getAreaActionClassMap() {
		return areaActionClassMap;
	}

	public Set<String> getAreas() {
		return areaActionClassMap.keySet();
	}
	
	/**
	 * @return the dbs
	 */
	public Set<String> getDbs() {
		return this.dbs;
	}
	
	/**
	 * @return the preProcessorClasses
	 */
	public List<String> getPreProcessorClasses() {
		return this.preProcessorClasses;
	}
	
	/**
	 * @return the postProcessorClasses
	 */
	public List<String> getPostProcessorClasses() {
		return this.postProcessorClasses;
	}
	
	/**
	 * @return the routerClass
	 */
	public String getRouterClass() {
		return routerClass;
	}
	
	/**
	 * @return the actionProcessorSuffixMap
	 */
	public Map<String, String> getActionProcessorSuffixMap() {
		return actionProcessorSuffixMap;
	}
}
