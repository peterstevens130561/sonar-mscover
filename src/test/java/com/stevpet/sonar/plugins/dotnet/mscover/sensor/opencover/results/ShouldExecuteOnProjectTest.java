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
package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.results;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.AbstractDotNetSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCoverageResultsSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
public class ShouldExecuteOnProjectTest  {

    private AbstractDotNetSensor sensor;
    private Project project; 
    private MsCoverProperties propertiesHelper;
    private VsTestEnvironment vsTestEnvironment;
    private FileSystemMock fileSystemMock = new FileSystemMock();
    @Before
    public void before() {
        project=mock(Project.class);
        MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
        propertiesHelper = mock(PropertiesHelper.class);
        vsTestEnvironment = new VsTestEnvironment();
        sensor = new OpenCoverCoverageResultsSensor(microsoftWindowsEnvironment,propertiesHelper, vsTestEnvironment,null);
          
    }
    
    @Test
    public void createSensor_expect() {
       assertNotNull(sensor);
    }
    
    @Test
    public void testCSProject_ShouldNotExecute() {
        givenProjectLanguageIsCSharp();
        when(project.isRoot()).thenReturn(false);
        assertFalse(sensor.shouldExecuteOnProject(project));
    }

    private void givenProjectLanguageIsCSharp() {
        fileSystemMock.givenLanguage("cs");
    }
    
    @Test
    public void regularCSProject_ShouldNotExecute() {
        givenProjectLanguageIsCSharp();
        when(project.isRoot()).thenReturn(false);
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
    
    @Test
    public void regularCSProjectOpenCoverMode_ShouldExecute() {
        givenProjectLanguageIsCSharp();
        when(project.isRoot()).thenReturn(true);
        when(propertiesHelper.runOpenCover()).thenReturn(true);
        when(propertiesHelper.getMode()).thenReturn("runvstest");
        vsTestEnvironment.setTestsHaveRun();
        assertTrue(sensor.shouldExecuteOnProject(project));
    }

    @Test
    public void regularOtherProjectOpenCoverMode_ShouldNotExecute() {
        when(project.getLanguageKey()).thenReturn("cpp");
        when(project.isRoot()).thenReturn(false);
        when(propertiesHelper.getMode()).thenReturn("runvstest");
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
    
    @Test
    public void regularCSRootProjectOpenCoverMode_ShouldNotExecute() {
        givenProjectLanguageIsCSharp();
        when(project.isRoot()).thenReturn(true);
        when(propertiesHelper.getMode()).thenReturn("runvstest");
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
}
