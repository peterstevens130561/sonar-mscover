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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class AssembliesFinderTestUtils {

    protected MsCoverProperties propertiesHelper;
    protected AssembliesFinder finder;
    protected List<VisualStudioProject> projects;
    protected VisualStudioProject project;
    private String buildConfiguration = "Debug";
    private String buildPlatform = "AnyCPU";


    protected void givenProjectIsNotAUnitTestProject() {
        when(project.isUnitTest()).thenReturn(false);
    }

    protected void mockSettingsToUseConfiguration() {
        when(propertiesHelper.getRequiredBuildConfiguration()).thenReturn(buildConfiguration);
        when(propertiesHelper.getRequiredBuildPlatform()).thenReturn(buildPlatform);
    }

    protected void givenProjectIsUnitTestProject(String projectName) {
        when(project.isUnitTest()).thenReturn(true);
        when(project.getArtifactName()).thenReturn(projectName);
    }

    protected void givenProjectDoesNotExistForConfigurationAndPlatform() {
        when(project.getArtifact(eq(buildConfiguration), eq(buildPlatform))).thenReturn(null);
    }

    protected void givenProjectExistsForConfigurationAndPlatform(String unitTestPath) {
        when(project.getArtifact(eq(buildConfiguration), eq(buildPlatform))).thenReturn(new File(unitTestPath));
    }

    protected String getPathToANonExistingDll(String name) {
        File unitTestDll=TestUtils.getResource("AssembliesFinder\\TestDll.txt");
        String unitTestPath=unitTestDll.getParent() + "\\" + name ;
        return unitTestPath;
    }

    protected void addNonExistingDllToIgnoreMissingList(String name) {
        ArrayList<String> missing = new ArrayList<String>();
        missing.add(name);
        when(propertiesHelper.getUnitTestAssembliesThatCanBeIgnoredIfMissing()).thenReturn(missing);
        when(propertiesHelper.isIgnoreMissingUnitTestAssembliesSpecified()).thenReturn(true);
    }

    protected String getPathToExistingUnitTestDll() {
        File unitTestDll=TestUtils.getResource("AssembliesFinder\\TestDll.txt");
        String unitTestPath=unitTestDll.getAbsolutePath();
        return unitTestPath;
    }

    protected List<String> fromBuildConfiguration(List<VisualStudioProject> projects) {
        return finder.findUnitTestAssembliesFromConfig(null, projects);
    }

}