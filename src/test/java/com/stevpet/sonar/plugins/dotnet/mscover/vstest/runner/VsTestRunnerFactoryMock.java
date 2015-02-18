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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import org.sonar.api.batch.fs.FileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.VsTestRunnerMock;

import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

public class VsTestRunnerFactoryMock extends GenericClassMock<AbstractVsTestRunnerFactory> {
    
    public VsTestRunnerFactoryMock() {
        super(AbstractVsTestRunnerFactory.class);
    }

    public void onCreate(VsTestRunnerMock vsTestRunnerMock) {
        // TODO Auto-generated method stub
        when(instance.createBasicTestRunnner(any(MsCoverProperties.class), 
                any(FileSystem.class),
                any(MicrosoftWindowsEnvironment.class)))
                .thenReturn(vsTestRunnerMock.getMock());
    }
}
