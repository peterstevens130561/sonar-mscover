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
package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.Sensor;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;

public class IntegrationTestCoverSensorTest {
    private MsCoverProperties propertiesHelper;
    private Project project;
    private Sensor sensor;
    private SonarCoverageHelperFactory sonarCoverageHelperFactory = new SonarCoverageHelperFactory();
    private MicrosoftWindowsEnvironmentMock microsoftwWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private MeasureSaverMock measureSaverMock = new MeasureSaverMock();
    @Before
    public void before() {       

        project = mock(Project.class);
        propertiesHelper = mock(PropertiesHelper.class);
        sensor= new IntegrationTestCoverSensor(propertiesHelper,sonarCoverageHelperFactory,null,microsoftwWindowsEnvironmentMock.getMock(),measureSaverMock.getMock());
        when(propertiesHelper.getMode()).thenReturn("reuse");
        when(propertiesHelper.getIntegrationTestsPath()).thenReturn("hi");
        when(propertiesHelper.isIntegrationTestsEnabled()).thenReturn(true);
    }
    
    @Test
    public void ShouldExecute_NullProject_False() {
        //Arrange
        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(null);
        //Assert
        Assert.assertFalse(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_IsRootDoExecuteRoot_False() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(true,false);        
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_IsNotRootDoExecuteRoot_False() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(false,true);      
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_IsRootDoExecuteRoot_True() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(true,true);        
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_IsNotRootDoNotExecuteRoot_True() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(false,false);      
        Assert.assertTrue(shouldExecute);
    }
    boolean arrangeAndActisRootExecuteRoot(boolean isRoot, boolean executeRoot) {
        when(project.isRoot()).thenReturn(isRoot);  
        when(propertiesHelper.excuteRoot()).thenReturn(executeRoot);
        return sensor.shouldExecuteOnProject(project);          
    }
    
}
