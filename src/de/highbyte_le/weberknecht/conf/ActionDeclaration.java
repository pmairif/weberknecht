/*
 * ActionDeclaration.java (weberknecht)
 *
 * Copyright 2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * created: 14.10.2012
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.conf;

/**
 * Action declaration attributes
 *
 * @author pmairif
 */
public class ActionDeclaration {

	private final String clazz;
	
	private final String preProcessorSet;

	private final String postProcessorSet;
	
	private final String errorHandlerClass;

	public ActionDeclaration(String clazz, String preProcessorSet, String postProcessorSet, String errorHandlerClass) {
		this.clazz = clazz;
		this.preProcessorSet = preProcessorSet;
		this.postProcessorSet = postProcessorSet;
		this.errorHandlerClass = errorHandlerClass;
	}

	public ActionDeclaration(String clazz) {
		this(clazz, null, null, null);
	}
	
	public String getClazz() {
		return clazz;
	}

	public String getPreProcessorSet() {
		return preProcessorSet;
	}

	public String getPostProcessorSet() {
		return postProcessorSet;
	}
	
	/**
	 * @return the errorHandlerClass
	 */
	public String getErrorHandlerClass() {
		return errorHandlerClass;
	}
	
	public boolean hasErrorHandlerClass() {
		return errorHandlerClass != null && errorHandlerClass.length() > 0;
	}

	@Override
	public String toString() {
		return "ActionDeclaration [clazz=" + clazz + ", preProcessorSet=" + preProcessorSet + ", postProcessorSet="
				+ postProcessorSet + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((postProcessorSet == null) ? 0 : postProcessorSet.hashCode());
		result = prime * result + ((preProcessorSet == null) ? 0 : preProcessorSet.hashCode());
		result = prime * result + ((errorHandlerClass == null) ? 0 : errorHandlerClass.hashCode());
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
		ActionDeclaration other = (ActionDeclaration) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		}
		else if (!clazz.equals(other.clazz))
			return false;
		if (postProcessorSet == null) {
			if (other.postProcessorSet != null)
				return false;
		}
		else if (!postProcessorSet.equals(other.postProcessorSet))
			return false;
		if (preProcessorSet == null) {
			if (other.preProcessorSet != null)
				return false;
		}
		else if (!preProcessorSet.equals(other.preProcessorSet))
			return false;
		if (errorHandlerClass == null) {
			if (other.errorHandlerClass != null)
				return false;
		}
		else if (!errorHandlerClass.equals(other.errorHandlerClass))
			return false;
		return true;
	}
}
