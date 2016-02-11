package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.Collection;

interface UnitTestsConfiguration {

	public enum Mode {
		DISABLED,
		ENABLED
	}
	
	public enum Tool {
		OPENCOVER,
		VSTEST
	}
	
	Mode getMode();
	
    String getUnitTestCoveragePath();


    String getUnitTestsAssemblies();

    String getTestSettings();



    /**
     * Gets the array of unit tests dlls that can be ignored if missing (A warning will still occur)
     * @return
     */
    Collection<String> getUnitTestAssembliesThatCanBeIgnoredIfMissing();

    Collection<String> getPdbsThatMayBeIgnoredWhenMissing();

    boolean isIgnoreMissingUnitTestAssembliesSpecified();

    String getOpenCoverInstallPath();

    String getUnitTestHintPath();
    
    boolean getOpenCoverSkipAutoProps();

    /**
     * @return path to installation dir of vstest.console.exe
     */
    String getVsTestInstallPath();
	
}
