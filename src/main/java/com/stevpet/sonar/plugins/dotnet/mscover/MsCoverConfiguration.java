/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration.RunMode;

public interface MsCoverConfiguration {

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

    /**
     * 
     * @return true when should run opencover
     */
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

    String getIntegrationTestsDir();

    /**
     * @return path to installation dir of vstest.console.exe
     */
    String getVsTestInstallPath();

    File getWorkSpaceRoot();


}