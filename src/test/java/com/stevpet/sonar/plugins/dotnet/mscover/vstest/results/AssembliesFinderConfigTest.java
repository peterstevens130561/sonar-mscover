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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.dotnetutils.UnitTestProjectFinderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.VisualStudioProjectMock;

public class AssembliesFinderConfigTest {

    
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
    private UnitTestProjectFinderMock unitTestProjectFinderMock = new UnitTestProjectFinderMock();
    private AssembliesFinder assembliesFinder;
    private String buildConfiguration = "Debug";
    private String buildPlatform="x64";
    private List<File> projectDirectories = new ArrayList<File>();
    File testResourceDir;

    @Before()
    public void before() {
        assembliesFinder = new AssembliesFinderFactory().create(msCoverPropertiesMock.getMock());
        AbstractAssembliesFinder finder = (AbstractAssembliesFinder) assembliesFinder;
        finder.setUnitTestProjectFinder(unitTestProjectFinderMock.getMock());
        testResourceDir=TestUtils.getResource("AssembliesFinderConfigTest");
        msCoverPropertiesMock.givenSolutionName("mySolution.sln");      
    }
    
    @Test
    public void SolutionHasNoUnitTestProjects() {
        List<File> projectDirectories = new ArrayList<File>();
        String pattern=".*UnitTest.*";
        unitTestProjectFinderMock.givenFindProjectDirectories(pattern,projectDirectories);
        msCoverPropertiesMock.givenRequiredBuildPlatform("x64");
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(testResourceDir);
        assertNotNull(assemblies);
        assertEquals(0,assemblies.size());
    }
    
    @Test
    public void SolutionHasOneUnitTestProject() {
        givenProject("BasicControls.UnitTest");
        String pattern=".*UnitTest.*";
  
        unitTestProjectFinderMock.givenFindProjectDirectories(pattern,projectDirectories);
        msCoverPropertiesMock.givenVisualStudioUnitTestPattern(pattern);
        msCoverPropertiesMock.givenRequiredBuildPlatform("x64");
        msCoverPropertiesMock.givenRequiredBuildConfiguration("Debug");
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(testResourceDir);
        assertNotNull(assemblies);
        assertEquals(1,assemblies.size());
    }
    
    private void givenProject(String relativePath) {
        projectDirectories.add(new File(testResourceDir,relativePath));
    }
}
    
