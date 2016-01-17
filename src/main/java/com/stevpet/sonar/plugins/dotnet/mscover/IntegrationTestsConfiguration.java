package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;

public interface IntegrationTestsConfiguration {

    public enum Mode {
    	DISABLED,RUN,READ
    }
    
    public enum Tool {
    	VSTEST,OPENCOVER
    }
    
	/**
	 * get the mode, plugins should use this to select where they should run or not
	 * @return
	 */
	Mode getMode();

	/**
	 * get the directory where coverage files are stored. Use when it has been verified that
	 * the mode is not disabled
	 * @return
	 * @throws IllegalArgumentException when not specified
	 */
	File getDirectory();

	/**
	 * get the tool to use when running or reading 
	 * @return
	 */
	Tool getTool();

	boolean matches(Tool tool, Mode mode);

}