/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.SolutionHasNoProjectsException;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.VisualStudioProjectMock;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class AssembliesFinderConfigTest {
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
    private AssembliesFinder assembliesFinder;
    private String buildConfiguration = "Debug";
    private String buildPlatform="x64";
    File testResourceDir;

    @Before()
    public void before() {
        assembliesFinder = new DefaultAssembliesFinder(msCoverPropertiesMock.getMock());
        testResourceDir=TestUtils.getResource("AssembliesFinderConfigTest");
        
    }
    
    @Test(expected=SolutionHasNoProjectsException.class)
    public void NoProjectList_ExpectSolutionHasNoProjects() {
        List<VisualStudioProject> projects = null;

        assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);  
        fail("expected exception");
    }
    @Test(expected=SolutionHasNoProjectsException.class)
    public void NoProject_ExpectSolutionHasNoProjects() {
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();

        assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);  
        fail("expected exception");
    }
    @Test
    public void NoSpecificSettingsAssemblyOnDefaultPlaceNotUnitTest_AssemblyNotFound() {
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        projects.add(visualStudioProjectMock.getMock());
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        assertNotNull(assemblies);
        assertEquals(0,assemblies.size());    
    }
    
    


    @Test
    public void NoSpecificSettingsAssemblyOnDefaultPlaceIsUnitTest_AssemblyFound() {
        List<VisualStudioProject> projects = givenUnitTestProject();
        visualStudioProjectMock.givenAssemblyName("somename");
        String path="solutiondir\\unittest.dll";
        File fut = givenArtifact(buildConfiguration, buildPlatform, path);
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        verifyFound(assemblies, fut);
    }
    
    @Test
    public void notFoundinArtifactButInConfigPlatform_AssemblyFound() {
        List<VisualStudioProject> projects = givenUnitTestProject();

        visualStudioProjectMock.givenDirectory(testResourceDir);
        visualStudioProjectMock.givenAssemblyName("somename");
        String bogusPath="solutiondir\\bindebug.dll";
        String expectedPath="\\bin\\Debug\\bindebug.dll";
        givenArtifact(buildConfiguration, buildPlatform, bogusPath);
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        verifyFound(assemblies, new File(testResourceDir,expectedPath));        
    }
    
    @Test
    public void notFoundinArtifactButInConfigPlatform_DoesNotMatchPatter() {
        List<VisualStudioProject> projects = givenUnitTestProject();

        visualStudioProjectMock.givenDirectory(testResourceDir);
        visualStudioProjectMock.givenAssemblyName("somename");
        String bogusPath="solutiondir\\bindebug.dll";
        givenArtifact(buildConfiguration, buildPlatform, bogusPath);
        assembliesFinder.setTestProjectPattern(Pattern.compile("doesnotmatch"));
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        assertEquals("should not have any assemblies, as the pattern does not match the assembly",0,assemblies.size()) ;   
    }
    
    @Test
    public void notFoundinArtifactButInConfigPlatform_DoesNotMatchFullyPatter() {
        List<VisualStudioProject> projects = givenUnitTestProject();

        visualStudioProjectMock.givenDirectory(testResourceDir);
        visualStudioProjectMock.givenAssemblyName("somename");
        String bogusPath="solutiondir\\bindebug.dll";
        givenArtifact(buildConfiguration, buildPlatform, bogusPath);
        assembliesFinder.setTestProjectPattern(Pattern.compile("name"));
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        assertEquals("should not have any assemblies, as the pattern does not match the assembly completely",0,assemblies.size()) ;   
    }
    
    @Test
    public void notFoundinArtifactButInConfigPlatform_MatchFullyPattern() {
        List<VisualStudioProject> projects = givenUnitTestProject();

        visualStudioProjectMock.givenDirectory(testResourceDir);
        visualStudioProjectMock.givenAssemblyName("somename");
        String bogusPath="solutiondir\\bindebug.dll";
        givenArtifact(buildConfiguration, buildPlatform, bogusPath);
        assembliesFinder.setTestProjectPattern(Pattern.compile(".*name.*"));
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        assertEquals("should have one assembly, as the pattern does matches the assembly completely",1,assemblies.size()) ;   
    }
    
    @Test
    public void notFindInArtifactButInHintPath_AssemblyFound() {
        List<VisualStudioProject> projects = givenUnitTestProject();
        visualStudioProjectMock.givenAssemblyName("somename");
        String hintPath=new File(testResourceDir,"hintpath").getAbsolutePath().replaceAll("\\\\","/");
        msCoverPropertiesMock.givenUnitTestHintPath(hintPath);
        String bogusPath="solutiondir\\hintpath.dll";
        String expectedPath="hintpath\\hintpath.dll";
        givenArtifact(buildConfiguration, buildPlatform, bogusPath);
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        verifyFound(assemblies, new File(testResourceDir,expectedPath));             
    }
    
    @Test
    public void notFoundInArtifactAndAnyWhereElseSpecifiedAsCanBeIgnored_ExpectIgnored() {
        
    }

    private List<VisualStudioProject> givenUnitTestProject() {
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        projects.add(visualStudioProjectMock.getMock());
        visualStudioProjectMock.givenIsUnitTest(true);
        visualStudioProjectMock.givenArtifactName("somename");
        msCoverPropertiesMock.givenRequiredBuildPlatform(buildPlatform);
        msCoverPropertiesMock.givenRequiredBuildConfiguration(buildConfiguration);
        return projects;
    }
 
    private File givenArtifact(String buildConfiguration, String buildPlatform,
            String path) {
        assertTrue(testResourceDir.exists());
        File fut=new File(testResourceDir,path);
        visualStudioProjectMock.givenArtifact(buildConfiguration,buildPlatform,fut);
        visualStudioProjectMock.givenArtifactName(fut);
        return fut;
    }

    private void verifyFound(List<String> assemblies, File fut) {
        assertNotNull(assemblies);
        assertEquals(1,assemblies.size()); 
        String found=assemblies.get(0);
        assertEquals(fut.getAbsolutePath(),found);
    }
}
