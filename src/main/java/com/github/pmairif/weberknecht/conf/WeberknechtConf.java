/*
 * WeberknechtConf.java (weberknecht)
 *
 * Copyright 2009-2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.conf;

import com.github.pmairif.weberknecht.request.routing.AreaPath;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * weberknecht configuration object
 * @author pmairif
 */
public class WeberknechtConf {
	/**
	 * Mapping of area paths to actions to declaration.
	 */
	private Map<AreaPath, Map<String, ActionDeclaration>> areaActionClassMap = new HashMap<AreaPath, Map<String, ActionDeclaration>>();

	private Map<AreaPath, String> defaultActions = new HashMap<AreaPath, String>();
	
	private Map<String, ProcessorList> preProcessors = new HashMap<String, ProcessorList>();
	private Map<String, ProcessorList> postProcessors = new HashMap<String, ProcessorList>();

	/**
	 * context names of used databases
	 */
	private Set<String> dbs = new HashSet<String>();
	
	private List<String> routerClasses = new Vector<String>();
	
	/**
	 * Mapping of suffixes to ActionViewProcessor classes.
	 */
	private Map<String, String> actionProcessorSuffixMap = new HashMap<String, String>();

	/**
	 * locale prefix
	 */
	private RoutingLocalePrefix routingLocalePrefix = null;
	
	/**
	 * Logger for this class
	 */
	private final static Logger log = LoggerFactory.getLogger(WeberknechtConf.class);

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
			readRoutingLocalePrefix(conf, rootElement);
			readActionViewProcessors(conf, rootElement);
			readActions(conf, rootElement, new AreaPath(), "", "", null);
			conf.checkActions();
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
	 * get the referred processor set id from the element. If no processor element is present, the parent value is used.
	 */
	private static String getProcessorSetId(Element element, String prefix, String parentValue) throws ConfigurationException {
		String val = parentValue;
		
		Element processorsElement = element.getChild(prefix+"-processors");
		if (processorsElement != null) {
			String ref = processorsElement.getAttributeValue("ref");
			if (null == ref)
				throw new ConfigurationException("Found old config! Define processor sets with IDs and ref to them with pre-/post-processor element and ref attribute!");
			
			val = ref;
		}
		
		return val;
	}

	/**
	 * get the defined error handler. If no such element is present, the parent value is used.
	 */
	private static String getErrorHandler(Element element, String parentValue) throws ConfigurationException {
		String val = parentValue;
		
		Element errHandlerElement = element.getChild("error-handler");
		if (errHandlerElement != null) {
			String clazz = errHandlerElement.getAttributeValue("class");
			if (null == clazz)
				throw new ConfigurationException("Got error handler without class!");
			
			val = clazz;
		}
		
		return val;
	}
	
	protected void checkActions() throws ConfigurationException {
		for (Map<String, ActionDeclaration> map: areaActionClassMap.values()) {
			for (ActionDeclaration ad: map.values()) {
				String preProcessorSet = ad.getPreProcessorSet();
				if (preProcessorSet != "" && !preProcessors.containsKey(preProcessorSet))
					throw new ConfigurationException("Pre processor '"+preProcessorSet+"' not found!");

				String postProcessorSet = ad.getPostProcessorSet();
				if (postProcessorSet != "" && !postProcessors.containsKey(postProcessorSet))
					throw new ConfigurationException("Post processor '"+postProcessorSet+"' not found!");
			}
		}
		
		if (areaActionClassMap.size() == 0) {
			log.error("checkActions() - There are no actions configured!");
		}
	}

	@SuppressWarnings("unchecked")
	protected static void readActions(WeberknechtConf conf, Element rootElement, AreaPath path, String parentPreId, String parentPostId, String parentErrorHandler) throws ConfigurationException {
		String preId = getProcessorSetId(rootElement, "pre", parentPreId);
		String postId = getProcessorSetId(rootElement, "post", parentPostId);
		String errHandler = getErrorHandler(rootElement, parentErrorHandler);
		
		List<Element> actionsElements = rootElement.getChildren("actions");
		if (actionsElements != null) {
			for (Element actionsElement: actionsElements) {
				String area = actionsElement.getAttributeValue("area");
				AreaPath subPath = path.fork(area);
				
				String defaultAction = actionsElement.getAttributeValue("default");
				if (defaultAction != null)	//don't delete existing defaults for areas with same name
					conf.defaultActions.put(subPath, defaultAction);
				
				readArea(conf, preId, postId, errHandler, actionsElement, subPath);

				readActions(conf, actionsElement, subPath, preId, postId, errHandler);
			}
		}
		
		List<Element> areaElements = rootElement.getChildren("area");
		if (areaElements != null) {
			for (Element areaElement: areaElements) {
				String area = areaElement.getAttributeValue("name");
				AreaPath subPath = path.fork(area);

				String defaultAction = areaElement.getAttributeValue("default");
				conf.defaultActions.put(subPath, defaultAction);

				readArea(conf, preId, postId, errHandler, areaElement, subPath);

				readActions(conf, areaElement, subPath, preId, postId, errHandler);
			}
		}
	}

	/**
	 * read area section
	 */
	protected static void readArea(WeberknechtConf conf, String rootPreId, String rootPostId, String rootErrHandler, Element areaElement, AreaPath path)
			throws ConfigurationException {
		String areaPreId = getProcessorSetId(areaElement, "pre", rootPreId);
		String areaPostId = getProcessorSetId(areaElement, "post", rootPostId);
		String areaErrHandler = getErrorHandler(areaElement, rootErrHandler);

		Map<String, ActionDeclaration> actionClassMap = conf.getActionClassMap(path);
		
		@SuppressWarnings("unchecked")
		List<Element> actionElements = areaElement.getChildren("action");
		if (null == actionElements) {
			log.error("readArea() - There are no actions configured for area \""+path+"\"");
		}
		else {
			for (Element e1: actionElements) {
				String name = e1.getAttributeValue("name");
				String className = e1.getAttributeValue("class");
				String preId = getProcessorSetId(e1, "pre", areaPreId);
				String postId = getProcessorSetId(e1, "post", areaPostId);
				String errHandler = getErrorHandler(e1, areaErrHandler);

				if (name != null && className != null) {
					ActionDeclaration declaration = new ActionDeclaration(className, preId, postId, errHandler);
					actionClassMap.put(name, declaration);
				}
				else {
					log.error("readArea() - invalid action configuration: name="+name+", class="+className);
				}
			}
		}
	}

	protected static void readRouter(WeberknechtConf conf, Element rootElement) {
		@SuppressWarnings("unchecked")
		List<Element> routerElements = rootElement.getChildren("router");
		if (routerElements != null) {
			for (Element routerElement: routerElements) {
				conf.routerClasses.add(routerElement.getAttributeValue("class"));
			}
		}
	}

	protected static void readRoutingLocalePrefix(WeberknechtConf conf, Element rootElement) {
		Element prefixElement = rootElement.getChild("routing-locale-prefix");
		if (prefixElement != null) {
			RoutingLocalePrefix prefix = new RoutingLocalePrefix();
			String optionalAtt = prefixElement.getAttributeValue("optional");
			prefix.setOptional(optionalAtt != null && optionalAtt.equals("true"));
			
			@SuppressWarnings("unchecked")
			List<Element> allowedElements = prefixElement.getChildren("allowed-value");
			if (allowedElements != null) {
				for (Element allowedElement: allowedElements) {
					prefix.addAllowedLocale(allowedElement.getTextNormalize());
				}
			}
			
			conf.setRoutingLocalePrefix(prefix);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected static Map<String, ProcessorList> readProcessorMap(
			WeberknechtConf conf, Element rootElement, String defTag, String processorTag
	) throws ConfigurationException {
		
		try {
			Map<String, ProcessorList> map = new HashMap<String, ProcessorList>();
			
			List<Element> postProcessorsElements = rootElement.getChildren(defTag);
			if (postProcessorsElements != null) {
				for (Element processorsElement: postProcessorsElements) {
					String id = processorsElement.getAttributeValue("id");
					if (null == id)
						throw new ConfigurationException("Found processor definition without id.");
					
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
		catch (ClassNotFoundException e) {
			throw new ConfigurationException("Problem instantiating processor", e);
		}
	}
	
	protected static void readProcessors(WeberknechtConf conf, Element rootElement) throws ConfigurationException {
		conf.preProcessors = readProcessorMap(conf, rootElement, "pre-processors-def", "pre-processor");
		conf.postProcessors = readProcessorMap(conf, rootElement, "post-processors-def", "post-processor");
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
				Map<String, ActionDeclaration> actionClassMap = conf.getActionClassMap(new AreaPath());	//actions for default area

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
		return getActionClassMap(new AreaPath());
	}
	
	/**
	 * get the action map for the area
	 */
	public synchronized Map<String, ActionDeclaration> getActionClassMap(AreaPath area) {
		AreaPath key = area != null ? area : new AreaPath();
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
	public Map<AreaPath, Map<String, ActionDeclaration>> getAreaActionClassMap() {
		return areaActionClassMap;
	}

	public Set<AreaPath> getAreas() {
		return areaActionClassMap.keySet();
	}
	
	/**
	 * @return the defaultActions
	 */
	public Map<AreaPath, String> getDefaultActions() {
		return defaultActions;
	}
	
	public String getDefaultAction(AreaPath area) {
		return defaultActions.get(area);
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
	 * @return the routerClass
	 */
	public List<String> getRouterClasses() {
		return routerClasses;
	}
	
	/**
	 * @return the actionProcessorSuffixMap
	 */
	public Map<String, String> getActionProcessorSuffixMap() {
		return actionProcessorSuffixMap;
	}

	/**
	 * @return the routingLocalePrefix
	 */
	public RoutingLocalePrefix getRoutingLocalePrefix() {
		return routingLocalePrefix;
	}
	
	public boolean hasRoutingLocalePrefix() {
		return routingLocalePrefix != null;
	}
	
	/**
	 * @param routingLocalePrefix the routingLocalePrefix to set
	 */
	public void setRoutingLocalePrefix(RoutingLocalePrefix routingLocalePrefix) {
		this.routingLocalePrefix = routingLocalePrefix;
	}
}
