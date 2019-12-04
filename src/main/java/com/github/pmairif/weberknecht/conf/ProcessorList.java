/*
 * ProcessorList.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 15.10.2012
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.conf;

import java.util.List;
import java.util.Vector;

import com.github.pmairif.weberknecht.request.processing.Processor;

/**
 * list of processor classes
 *
 * @author pmairif
 */
public class ProcessorList {

	private final String id;
	
	private final List<Class<? extends Processor>> processorClasses;
	
	public ProcessorList(String id) {
		this.id = id;
		this.processorClasses = new Vector<Class<? extends Processor>>();
	}

	public ProcessorList(String id, List<Class<? extends Processor>> processorClasses) {
		this.id = id;
		this.processorClasses = processorClasses;
	}

	@SuppressWarnings("unchecked")
	public void addProcessorClass(String clazzName) throws ClassNotFoundException, ConfigurationException {
		try {
			Class<?> clazz = Class.forName(clazzName);
			if (!(clazz.newInstance() instanceof Processor))
				throw new ConfigurationException(clazzName + " is not a processor");
			processorClasses.add((Class<? extends Processor>)Class.forName(clazzName));
		}
		catch (InstantiationException e) {
			throw new ConfigurationException("problem instantiating processor", e);
		}
		catch (IllegalAccessException e) {
			throw new ConfigurationException("problem instantiating processor", e);
		}
	}

	public void addProcessorClass(Class<? extends Processor> clazz) {
		processorClasses.add(clazz);
	}
	
	public String getId() {
		return id;
	}

	public List<Class<? extends Processor>> getProcessorClasses() {
		return processorClasses;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessorList other = (ProcessorList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
