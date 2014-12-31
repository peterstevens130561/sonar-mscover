/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.Collection;
import java.util.List;

import org.sonar.api.resources.Project;

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

    public abstract boolean isTestProject(Project project);

    public abstract String getVisualStudioUnitTestPattern();


}