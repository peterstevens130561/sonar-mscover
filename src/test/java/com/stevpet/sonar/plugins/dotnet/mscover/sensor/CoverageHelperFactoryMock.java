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

import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

import static org.mockito.Mockito.*;


public class CoverageHelperFactoryMock extends GenericClassMock<AbstractCoverageHelperFactory> {

    public CoverageHelperFactoryMock() {
        super(AbstractCoverageHelperFactory.class);
    }

    public void WhenCreateShouldExecuteHelper(ShouldExecuteHelperMock mock ) {
        when(instance.createShouldExecuteHelper(any(MsCoverProperties.class))).thenReturn(mock.getMock());
    }

    public void whencreateIntegrationTestCoverageHelper(
            CoverageHelperMock coverageHelperMock) {
        when(instance.createVsTestIntegrationTestCoverageHelper(any(FileSystem.class), any(MeasureSaver.class))).thenReturn(coverageHelperMock.getMock());
    }
}
