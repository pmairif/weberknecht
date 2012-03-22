/*
 * Version.java
 *
 * Copyright 2007-2012 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 * 
 * created: 2007-09-01
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Versions are denoted using a standard triplet of integers: MAJOR.MINOR.PATCH.
 * The basic intent is that MAJOR versions are incompatible, large-scale upgrades of the API.
 * MINOR versions retain source and binary compatibility with older minor versions,
 * and changes in the PATCH level are perfectly compatible, forwards and backwards.
 * 
 * @author pmairif
 */
public class Version {
	/**
	 * Logger for this class
	 */
	private final static Log logger = LogFactory.getLog(Version.class);

    private static Version appVersion = null;
    
	private final int major;
	private final int minor;
	private final int patch;
	
	/**
	 * optional build number
	 */
	private final Integer build;
	
	/**
	 * optional branch info
	 */
	private final String branch;
	
    @SuppressWarnings("nls")
    public static synchronized Version getAppVersion() {
    	if (null == appVersion) {
    		try {
        		InputStream in = Version.class.getResourceAsStream("version.properties"); //$NON-NLS-1$
        		if (in != null) {
	        		Properties p = new Properties();
					p.load(in);

					appVersion = parseProperties(p);
        		}
        		else {
        			logger.error("getAppVersion() - version.properties not found");
        		}
			}
			catch (IOException e) {
				logger.error("getAppVersion() - I/O Exception while loading version.properties: "+e.getMessage());
			}
    	}
    	
    	return appVersion;
    }

    protected static Version parseProperties(Properties properties) {
		try {
			int major = Integer.parseInt( properties.getProperty("version.weberknecht.major") );
			int minor = Integer.parseInt( properties.getProperty("version.weberknecht.minor") );
			int patch = Integer.parseInt( properties.getProperty("version.weberknecht.patch") );
			
			String buildString = properties.getProperty("version.weberknecht.build");
			Integer build = null;
			if (buildString != null)
				build = new Integer( buildString );
			
			String branch = properties.getProperty("version.weberknecht.branch");
			
			return new Version(major, minor, patch, build, branch);
		}
		catch (NumberFormatException e) {
			logger.error("parseProperties() - number format exception: "+e.getMessage());
		}
		
		return null;
    }
    
	public Version(int major, int minor, int patch) {
		this(major, minor, patch, null, null);
	}

	public Version(int major, int minor, int patch, Integer build, String branch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.build = build;
		this.branch = branch;
	}
	
	public int getMajor() {
		return this.major;
	}
	
	public int getMinor() {
		return this.minor;
	}
	
	public int getPatch() {
		return this.patch;
	}
	
	public boolean hasBuildNumber() {
		return (build != null);
	}
	
	public int getBuildNumber() {
		if (build != null)
			return build.intValue();
		return 0;
	}
	
	public boolean hasBranch() {
		return (branch != null);
	}

	/**
	 * @return the branch
	 */
	public String getBranch() {
		return branch;
	}
	
	@Override
	public String toString() {
		return getVersionString(VersionFormat.FULL);
	}

	/**
	 * create a version string
	 */
	public String getVersionString(VersionFormat format) {
		StringBuilder b = new StringBuilder();
		
		b.append(major).append(".").append(minor); //$NON-NLS-1$
		
		if (format != VersionFormat.SHORT) {
			b.append(".").append(patch); //$NON-NLS-1$
			
			if (hasBranch())
				b.append("-").append(branch);
			
			if (format != VersionFormat.DEFAULT && hasBuildNumber())
				b.append(" b").append(getBuildNumber()); //$NON-NLS-1$
		}
		
		return b.toString();
	}
	
	public enum VersionFormat {
		/**
		 * version string with just the major and minor number
		 */
		SHORT,

		/**
		 * version string with the major, minor and patch number
		 */
		DEFAULT,

		/**
		 * version string with the full version information including build number but without state
		 */
		FULL		
	}
}
