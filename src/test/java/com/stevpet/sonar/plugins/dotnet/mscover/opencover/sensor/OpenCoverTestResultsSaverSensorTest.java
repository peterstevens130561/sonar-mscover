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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverTestResultsSaverSensorTest {

    MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    TimeMachineMock timeMachineMock = new TimeMachineMock();
    
    MicrosoftWindowsEnvironment microsoftWindowsEnvironment ;
    MsCoverProperties msCoverProperties;
    private VsTestEnvironment vsTestEnvironment;
    
    @Before
    public void before() {
        microsoftWindowsEnvironment = microsoftWindowsEnvironmentMock.getMock();
        msCoverProperties = msCoverPropertiesMock.getMock();
        vsTestEnvironment = vsTestEnvironmentMock.getMock();
    }
    
    @Test
    public void getSupportedLanguages_ExpectCsOnly() {
        OpenCoverTestResultsSaverSensor sensor = new OpenCoverTestResultsSaverSensor(microsoftWindowsEnvironment, 
                msCoverProperties,vsTestEnvironment,null,null);
        String[] languages = sensor.getSupportedLanguages();
        assertNotNull(languages);
        assertEquals("cs",languages[0]);
        
        
    }
}
