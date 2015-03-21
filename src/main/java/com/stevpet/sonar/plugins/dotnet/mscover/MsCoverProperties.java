package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.Collection;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;

public interface MsCoverProperties {

    public abstract boolean isIntegrationTestsEnabled();

    public abstract boolean isUnitTestsEnabled();

    public abstract String getIntegrationTestsPath();

    public abstract boolean excuteRoot();

    public abstract String getUnitTestCoveragePath();

    public abstract String getExclusions();

    public abstract boolean isPluginEnabled();

    public abstract String getInclusions();

    public abstract String getCutOffDate();

    public abstract String getIntegrationTestResultsPath();

    public abstract String getUnitTestResultsPath();

    public abstract String getMode();

    public abstract String getUnitTestsAssemblies();

    public abstract String getTestSettings();

    public abstract boolean shouldMsCoverRun();

    public abstract boolean isCPlusPlus();

    public abstract List<String> getLanguages();

    public abstract RunMode getRunMode();

    public abstract boolean runOpenCover();

    public abstract boolean runVsTest();

    /**
     * gets the value of sonar.dotnet.buildConfiguration. If the value is not set exception is thrown
     */
    public abstract String getRequiredBuildConfiguration();

    /**
     * gets the value of sonar.dotnet.buildPlatform. If the value is not set exception is thrown
     * @return
     */
    public abstract String getRequiredBuildPlatform();

    /**
     * Gets the array of unit tests dlls that can be ignored if missing (A warning will still occur)
     * @return
     */
    public abstract Collection<String> getUnitTestAssembliesThatCanBeIgnoredIfMissing();

    public abstract Collection<String> getPdbsThatMayBeIgnoredWhenMissing();

    public abstract boolean isIgnoreMissingUnitTestAssembliesSpecified();

    public abstract String getOpenCoverInstallPath();

    public abstract String getUnitTestHintPath();
    
    public abstract boolean getOpenCoverSkipAutoProps();

    public abstract String getVsTestDir();
    
    /**
     * @return true if property set to true, false in any other case
     */
    public abstract boolean getVsTestInIsolation();


}