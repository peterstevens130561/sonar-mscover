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
package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.results;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.SensorContextMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class OpenCoverCoverageResultsTest extends
        OpenCoverCoverageResultsBaseTest {
    
    private SensorContextMock sensorContextMock = new SensorContextMock();

    @Before() 
    public void before() {
        super.before();
    }
    
    @Test() 
    public void createWithEmptyMocks_ShouldCreate(){
        assertNotNull("sensor should be created with default mocks",sensor);
        
    }
    
    @Test
    public void shouldExecute_OpenCoverSet_True() {
        msCoverPropertiesMock.givenRunOpenCover(true);
        projectMock.givenIsRootProject(true);
        boolean result=sensor.shouldExecuteOnProject(projectMock.getMock());
        assertTrue("Configuration set to run opencover",result);
    }
    
    @Test
    public void shouldExecute_NotOpenCover_False() {
        msCoverPropertiesMock.givenRunOpenCover(false);
        projectMock.givenIsRootProject(true);
        boolean result=sensor.shouldExecuteOnProject(projectMock.getMock());
        assertFalse("Invoked on project,rather than solution",result);
    }
    @Test
    public void shouldExecute_NotRootProject_False() {
        msCoverPropertiesMock.givenRunOpenCover(true);
        projectMock.givenIsRootProject(false);
        boolean result=sensor.shouldExecuteOnProject(projectMock.getMock());
        assertFalse("Invoked on project,rather than solution",result);
    }
    
    @Test
    public void analyse_EmptyCoverage_NoSave() {
        vsTestEnvironmentMock.givenTestsHaveNotExecuted();
        vsTestEnvironmentMock.givenSonarCoverage(createEmptyCoverageFile());
        sensor.analyse(projectMock.getMock(),sensorContextMock.getMock());
        measureSaverMock.verifySaveFileMeasure(0, new Measure());
        
    }
    
    @Test
    public void analyse_TwoFiles_TwoSaves() {
        vsTestEnvironmentMock.givenTestsHaveExecuted();
        vsTestEnvironmentMock.givenSonarCoverage(createSimpleCoverageFile());
        sensor.analyse(projectMock.getMock(),sensorContextMock.getMock());
        measureSaverMock.verifyInvokedSaveFileMeasure(2,CoreMetrics.LINES_TO_COVER);
        measureSaverMock.verifyInvokedSaveFileMeasure(2,CoreMetrics.UNCOVERED_LINES);
        measureSaverMock.verifyInvokedSaveFileMeasure(2,CoreMetrics.COVERAGE);
        measureSaverMock.verifyInvokedSaveFileMeasure(2,CoreMetrics.LINE_COVERAGE);
    }
    
    public SonarCoverage createEmptyCoverageFile() {
        return new SonarCoverage();
    }
    public SonarCoverage createSimpleCoverageFile() {
        SonarCoverage coverage = new SonarCoverage();
        SonarFileCoverage coveredFile = coverage.getCoveredFile("1");
        
        coveredFile.addLinePoint(1, false);
        coveredFile.addLinePoint(2, true);
        coveredFile.setAbsolutePath("file1");
        coveredFile = coverage.getCoveredFile("2");
        coveredFile.addLinePoint(1, false);
        coveredFile.addLinePoint(10, true);
        coveredFile.addLinePoint(20, true);
        coveredFile.setAbsolutePath("file2");
        return coverage;
    }

}
