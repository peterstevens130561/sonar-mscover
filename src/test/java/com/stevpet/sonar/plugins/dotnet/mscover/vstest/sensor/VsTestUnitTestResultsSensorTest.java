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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.SensorContextMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;


public class VsTestUnitTestResultsSensorTest {
    private TestWrapper classUnderTest = new TestWrapper();
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private TimeMachineMock timeMachineMock = new TimeMachineMock();
    private VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private ProjectMock projectMock = new ProjectMock();
    private SensorContextMock sensorContextMock = new SensorContextMock();
    private VsTestUnitTestResultsAnalyserMock vsTestUnitTestResultsAnalyserMock = new VsTestUnitTestResultsAnalyserMock();
    
    @Before
    public void before() {
        
    }
    
    @Test
    public void analyse_shouldpass() {
       classUnderTest.givenNewSensor(microsoftWindowsEnvironmentMock,msCoverPropertiesMock,timeMachineMock,vsTestEnvironmentMock);
       classUnderTest.givenVsTestUnitTestResultsAnalyserMock(vsTestUnitTestResultsAnalyserMock);
       classUnderTest.analyse(projectMock,sensorContextMock);
    }
}
