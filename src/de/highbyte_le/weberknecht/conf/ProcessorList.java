/*
 * ProcessorList.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 15.10.2012
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.conf;

import java.util.List;
import java.util.Vector;

/**
 * list of processor classes
 *
 * @author pmairif
 */
public class ProcessorList {

	private final String id;
	
	private final List<String> processorClasses;
	
	public ProcessorList(String id) {
		this.id = id;
		this.processorClasses = new Vector<String>();
	}

	public ProcessorList(String id, List<String> processorClasses) {
		this.id = id;
		this.processorClasses = processorClasses;
	}

	public void addProcessorClass(String clazz) {
		processorClasses.add(clazz);
	}

	public String getId() {
		return id;
	}

	public List<String> getProcessorClasses() {
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
