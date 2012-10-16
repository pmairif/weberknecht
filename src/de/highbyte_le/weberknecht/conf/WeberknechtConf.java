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
	 * Mapping of areas to actions to declaration.
	 * Default area is represented by the empty string.
	 */
	private Map<String, Map<String, ActionDeclaration>> areaActionClassMap = new HashMap<String, Map<String, ActionDeclaration>>();

	private Map<String, ProcessorList> preProcessors = new HashMap<String, ProcessorList>();
	private Map<String, ProcessorList> postProcessors = new HashMap<String, ProcessorList>();

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
	public static WeberknechtConf readConfig(ServletContext servletContext) throws ConfigurationException {
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
	public static WeberknechtConf readConfig(InputStream in) throws ConfigurationException {
		WeberknechtConf conf = new WeberknechtConf();
		
		Document doc = null;
		try {
			doc = new SAXBuilder().build(in);
			Element rootElement = doc.getRootElement();
			
			readDbs(conf, rootElement);
			readProcessors(conf, rootElement);
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
	 * get the referred processor set id from the element. If the attribute is not set, the parent value is used.
	 */
	private static String getProcessorSetId(Element element, String attributeName, String parentValue) {
		String val = element.getAttributeValue(attributeName);
		if (null == val)
			val = parentValue;

		return val;
	}
	
	@SuppressWarnings("unchecked")
	protected static void readActions(WeberknechtConf conf, Element rootElement) {
		String rootPreId = getProcessorSetId(rootElement, "pre", "");
		String rootPostId = getProcessorSetId(rootElement, "post", "");
		
		List<Element> actionsElements = rootElement.getChildren("actions");
		if (actionsElements != null) {
			for (Element actionsElement: actionsElements) {
				String areaPreId = getProcessorSetId(actionsElement, "pre", rootPreId);
				String areaPostId = getProcessorSetId(actionsElement, "post", rootPostId);

				String area = actionsElement.getAttributeValue("area");
				if (area == null)
					area = "";
				Map<String, ActionDeclaration> actionClassMap = conf.getActionClassMap(area);
				
				List<Element> actionElements = actionsElement.getChildren("action");
				if (null == actionElements) {
					log.error("readActions() - There are no actions configured for area \""+area+"\"");
				}
				else {
					for (Element e1: actionElements) {
						String name = e1.getAttributeValue("name");
						String className = e1.getAttributeValue("class");
						String preId = getProcessorSetId(e1, "pre", areaPreId);
						String postId = getProcessorSetId(e1, "post", areaPostId);

						if (name != null && className != null) {
							ActionDeclaration declaration = new ActionDeclaration(className, preId, postId);
							actionClassMap.put(name, declaration);
						}
						else {
							log.error("readConfig() - invalid action configuration: name="+name+", class="+className);
						}
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

	@SuppressWarnings("unchecked")
	protected static Map<String, ProcessorList> readProcessorMap(WeberknechtConf conf, Element rootElement, String setTag, String processorTag) throws ConfigurationException {
		Map<String, ProcessorList> map = new HashMap<String, ProcessorList>();
		
		List<Element> postProcessorsElements = rootElement.getChildren(setTag);
		if (postProcessorsElements != null) {
			for (Element processorsElement: postProcessorsElements) {
				String id = processorsElement.getAttributeValue("id");
				if (null == id)
					throw new ConfigurationException("Found old config! Add IDs to your processor sets and refer to them!");
				
				ProcessorList processors = new ProcessorList(id);
				
				List<Element> postProcessorElements = processorsElement.getChildren(processorTag);
				if (postProcessorElements != null) {
					for (Element e: postProcessorElements) {
						String className = e.getAttributeValue("class");
						processors.addProcessorClass(className);
					}
				}
				
				map.put(id, processors);
			}
		}
		
		return map;
	}
	
	protected static void readProcessors(WeberknechtConf conf, Element rootElement) throws ConfigurationException {
		conf.preProcessors = readProcessorMap(conf, rootElement, "pre-processors", "pre-processor");
		conf.postProcessors = readProcessorMap(conf, rootElement, "post-processors", "post-processor");
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
				Map<String, ActionDeclaration> actionClassMap = conf.getActionClassMap("");	//actions for default area

				for (Element e: actionElements) {
					String name = e.getAttributeValue("name");
					String className = e.getAttributeValue("class");
					
					if (name != null && className != null)
						actionClassMap.put(name, new ActionDeclaration(className));
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
	public Map<String, ActionDeclaration> getActionClassMap() {
		return getActionClassMap("");
	}
	
	/**
	 * get the action map for the area
	 */
	public synchronized Map<String, ActionDeclaration> getActionClassMap(String area) {
		String key = area != null ? area : "";
		Map<String, ActionDeclaration> ret = areaActionClassMap.get(key);
		
		if (null == ret) {
			ret = new HashMap<String, ActionDeclaration>();
			areaActionClassMap.put(key, ret);
		}
		
		return ret;
	}
	
	/**
	 * @return the areaActionClassMap
	 */
	public Map<String, Map<String, ActionDeclaration>> getAreaActionClassMap() {
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
	 * get map of IDs to pre processor lists
	 */
	public Map<String, ProcessorList> getPreProcessorListMap() {
		return this.preProcessors;
	}
	
	/**
	 * get map of IDs to post processor lists
	 */
	public Map<String, ProcessorList> getPostProcessorListMap() {
		return this.postProcessors;
	}
	
	/**
	 * find the pre processor set to be applied to the given action
	 */
	public ActionDeclaration findActionDeclaration(String area, String action) {
		Map<String, ActionDeclaration> actionClassMap = areaActionClassMap.get(area);
		if (actionClassMap != null)
			return actionClassMap.get(action);
		return null;
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
