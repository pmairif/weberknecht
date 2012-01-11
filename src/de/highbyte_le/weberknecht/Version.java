/*
 * Version.java
 *
 * Copyright 2007 Patrick Mairif.
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
	
    @SuppressWarnings("nls")
    public static synchronized Version getAppVersion() {
    	if (null == appVersion) {
    		try {
        		InputStream in = Version.class.getResourceAsStream("version.properties"); //$NON-NLS-1$
        		if (in != null) {
	        		Properties p = new Properties();
					p.load(in);

					try {
						int major = Integer.parseInt( p.getProperty("version.weberknecht.major") );
						int minor = Integer.parseInt( p.getProperty("version.weberknecht.minor") );
						int patch = Integer.parseInt( p.getProperty("version.weberknecht.patch") );
						int build = Integer.parseInt( p.getProperty("version.weberknecht.build") );
						Version.VersionState versionState = Version.VersionState.parseString(p.getProperty("version.weberknecht.state"));
						
						appVersion = new Version(major, minor, patch, build, versionState);
					}
					catch (NumberFormatException e) {
						logger.error("loadProperties() - number format exception: "+e.getMessage());
					}
        		}
        		else {
        			logger.error("createVersion() - version.properties not found");
        		}
			}
			catch (IOException e) {
				logger.error("createVersion() - I/O Exception while loading version.properties: "+e.getMessage());
			}
    	}
    	
    	return appVersion;
    }
    
	
	public Version(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	public Version(int major, int minor, int patch, int build) {
		this(major, minor, patch);
		this.build = new Integer(build);
	}

	public Version(int major, int minor, int patch, VersionState state) {
		this(major, minor, patch);
		this.state = state;
	}

	public Version(int major, int minor, int patch, int build, VersionState state) {
		this(major, minor, patch, build);
		this.state = state;
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
	
	public boolean hasVersionState() {
		return (state != null);
	}
	
	public VersionState getVersionState() {
		return state;
	}
	
	@Override
	public String toString() {
		return getVersionString(VersionFormat.FULL_WITH_STATE_ASCII);
	}

	/**
	 * create a version string
	 */
	public String getVersionString(VersionFormat format) {
		StringBuilder b = new StringBuilder();
		
		b.append(major).append(".").append(minor); //$NON-NLS-1$
		
		if (format != VersionFormat.SHORT) {
			b.append(".").append(patch); //$NON-NLS-1$
			
			if (format != VersionFormat.DEFAULT && hasBuildNumber()) {
				b.append(" b").append(getBuildNumber()); //$NON-NLS-1$
			}
			
			if (format == VersionFormat.FULL_WITH_STATE_ASCII && hasVersionState()) {
				b.append(" (").append(state.getAscii()).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else if (format == VersionFormat.FULL_WITH_STATE_GREEK && hasVersionState()) {
				b.append(" (").append(state.getGreek()).append(")");  //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		
		return b.toString();
	}
	
	private int major = 0;
	private int minor = 0;
	private int patch = 0;
	
	/**
	 * optional build number
	 */
	private Integer build = null;
	
	/**
	 * optional version state
	 */
	private VersionState state = null;
	
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
		FULL,
		
		/**
		 * with the full version information with state in ASCII
		 */
		FULL_WITH_STATE_ASCII,
		
		/**
		 * with the full version information with state in Greek letters
		 */
		FULL_WITH_STATE_GREEK
	}
	
    public enum VersionState {
    	ALPHA,
    	BETA,
    	GAMMA;
    	
    	public String getGreek() {
    		String greek = null;
    		switch (this) {
    			case ALPHA: greek = "α"; break; //$NON-NLS-1$
    			case BETA:	greek = "β"; break; //$NON-NLS-1$
    			case GAMMA:	greek = "γ"; break; //$NON-NLS-1$
    		}
    		return greek;
    	}

    	public String getAscii() {
    		String ascii = null;
    		switch (this) {
    			case ALPHA: ascii = "alpha"; break; //$NON-NLS-1$
    			case BETA:	ascii = "beta"; break; //$NON-NLS-1$
    			case GAMMA:	ascii = "gamma"; break; //$NON-NLS-1$
    		}
    		return ascii;
    	}
    	
    	@SuppressWarnings("nls")
    	public static VersionState parseString(String stateString) {
    		if (stateString.equalsIgnoreCase("alpha") || stateString.equals("α"))
    			return ALPHA;
    		if (stateString.equalsIgnoreCase("beta") || stateString.equals("β"))
    			return BETA;
    		if (stateString.equalsIgnoreCase("gamma") || stateString.equals("γ"))
    			return GAMMA;
    		return null;
    	}
    }
}
