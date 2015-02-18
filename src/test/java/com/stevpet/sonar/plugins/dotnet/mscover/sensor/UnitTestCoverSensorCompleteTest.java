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
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunnerTestUtils;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsSensor;

public class UnitTestCoverSensorCompleteTest {
    private Project project;
    private VsTestEnvironment vsTestEnvironment;
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private ResourceMediatorMock resourceMediatorMock = new ResourceMediatorMock();

    @Before 
    public void before() {
        project = mock(Project.class);
    }


    @Test
    public void projectIsRootPropertyExecuteRootIsTrue_ShouldNotExecute() {
        MsCoverProperties propertiesHelper = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        VsTestUnitTestResultsSensor sensor = createSensor(propertiesHelper);
        when(project.isRoot()).thenReturn(true);
        
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        Assert.assertFalse(shouldExecute);
    }


    private VsTestUnitTestResultsSensor createSensor(
            MsCoverProperties propertiesHelper) {
        VsTestUnitTestResultsSensor sensor = new VsTestUnitTestResultsSensor(propertiesHelper,vsTestEnvironment,null,microsoftWindowsEnvironmentMock.getMock(),resourceMediatorMock.getMock());
        return sensor;
    }
    
    @Test
    public void runTestsNotRoot_ShouldExecute() {
        MsCoverProperties propertiesHelper = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        VsTestUnitTestResultsSensor sensor = createSensor(propertiesHelper);
        when(project.isRoot()).thenReturn(false);
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        Assert.assertTrue(shouldExecute);
    }
}
