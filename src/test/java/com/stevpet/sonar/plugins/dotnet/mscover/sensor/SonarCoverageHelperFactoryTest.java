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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class SonarCoverageHelperFactoryTest {

    private MeasureSaverMock measureSaverMock = new MeasureSaverMock();
    private MeasureSaver measureSaver;
    private FileSystemMock fileSystemMock = new FileSystemMock();
    private AbstractCoverageHelperFactory factory ;
    
    @Before
    public void before() {
        measureSaver = measureSaverMock.getMock();
        factory = new SonarCoverageHelperFactory() ;
    }

    @Test

    public void createIntegrationTestCoverageHelper_DoesNotActOnDependencies() {
        CoverageSaver coverageHelper = factory.createVsTestIntegrationTestCoverageHelper(fileSystemMock.getMock(), measureSaver);
        assertNotNull(coverageHelper);

    }
    
    @Test
    public void createUnitTestCoverageHelper_DoesNotActOnDependencies() {
        CoverageSaver coverageHelper = factory.createUnitTestCoverageHelper(fileSystemMock.getMock(), measureSaver);
        assertNotNull(coverageHelper);
    }
}
