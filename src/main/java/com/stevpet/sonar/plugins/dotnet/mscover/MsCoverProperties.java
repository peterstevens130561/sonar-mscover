package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.Collection;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;

public interface MsCoverProperties {

    boolean isIntegrationTestsEnabled();

    boolean isUnitTestsEnabled();

    String getIntegrationTestsPath();

    boolean excuteRoot();

    String getUnitTestCoveragePath();

    String getExclusions();

    boolean isPluginEnabled();

    String getInclusions();

    String getCutOffDate();

    String getIntegrationTestResultsPath();

    String getUnitTestResultsPath();

    String getMode();

    String getUnitTestsAssemblies();

    String getTestSettings();

    boolean shouldMsCoverRun();

    boolean isCPlusPlus();

    List<String> getLanguages();

    RunMode getRunMode();

    boolean runOpenCover();

    boolean runVsTest();

    /**
     * gets the value of sonar.dotnet.buildConfiguration. If the value is not set exception is thrown
     */
    String getRequiredBuildConfiguration();

    /**
     * gets the value of sonar.dotnet.buildPlatform. If the value is not set exception is thrown
     * @return
     */
    String getRequiredBuildPlatform();

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


}