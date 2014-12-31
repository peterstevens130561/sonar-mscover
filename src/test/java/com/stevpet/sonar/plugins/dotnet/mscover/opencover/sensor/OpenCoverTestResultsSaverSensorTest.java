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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.TimeMachine;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverTestResultsSaverSensorTest {

    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    TimeMachineMock timeMachineMock = new TimeMachineMock();
    
    MsCoverProperties msCoverProperties;
    private VsTestEnvironment vsTestEnvironment;
    private TimeMachine timeMachine;
    
    @Before
    public void before() {
        msCoverProperties = msCoverPropertiesMock.getMock();
        vsTestEnvironment = vsTestEnvironmentMock.getMock();
        timeMachine = timeMachineMock.getMock();
    }
    
    @Test
    public void getSupportedLanguages_ExpectCsOnly() {
        OpenCoverTestResultsSaverSensor sensor = new OpenCoverTestResultsSaverSensor( msCoverProperties,vsTestEnvironment,timeMachine);
        String[] languages = sensor.getSupportedLanguages();
        
    }
}
