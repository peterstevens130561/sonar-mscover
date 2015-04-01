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
package com.stevpet.sonar.plugins.dotnet.mscover;


import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCommandMock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyListOf;

public class OpenCoverCommandBuilderMock extends GenericClassMock<OpenCoverCommandBuilder> {
    public OpenCoverCommandBuilderMock() {
        super(OpenCoverCommandBuilder.class);
    }

    public void verifySetOpenCovercommand(
            OpenCoverCommandMock openCoverCommandMock) {
        verify(instance,times(1)).setOpenCoverCommand(openCoverCommandMock.getMock());
    }

    public void verifySetAssemblies() {
        verify(instance,times(1)).setAssemblies(anyListOf(String.class)); 
    }
    
    public void verifySetMsCoverProperties(MsCoverPropertiesMock msCoverPropertiesMock) {
        verify(instance,times(1)).setMsCoverProperties(msCoverPropertiesMock.getMock());
    }
    
    public void verifySetTestRunner(VsTestRunnerCommandBuilderMock vsTestRunnerBuilderMock) {
        verify(instance,times(1)).setTestRunner(vsTestRunnerBuilderMock.getMock());
    }
}
