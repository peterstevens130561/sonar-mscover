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

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.SensorContextMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;

public class TestWrapper {

    private VsTestUnitTestResultsSensor sensor;
    public void givenNewSensor(
            MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock,
            MsCoverPropertiesMock msCoverPropertiesMock,
            TimeMachineMock timeMachineMock,
            VsTestEnvironmentMock vsTestEnvironmentMock) {
        
        sensor=new VsTestUnitTestResultsSensor(
                msCoverPropertiesMock.getMock(),
                vsTestEnvironmentMock.getMock(),null,
                microsoftWindowsEnvironmentMock.getMock(),null);
                
    }
    
    public void analyse(ProjectMock projectMock,
            SensorContextMock sensorContextMock) {
        sensor.analyse(projectMock.getMock(), sensorContextMock.getMock());
    }


    public void givenVsTestUnitTestResultsAnalyserMock(
            VsTestUnitTestResultsAnalyserMock vsTestUnitTestResultsAnalyserMock) {
        sensor.setVsTestUnitTestResultsAnalyser(vsTestUnitTestResultsAnalyserMock.getMock());
        
    }

}
