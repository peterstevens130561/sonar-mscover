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
package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.CoreMetrics;

import com.stevpet.sonar.plugins.dotnet.mscover.model.TestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModelMock;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeamMock;

public class TestResultsSaverTest {
    
    MeasureSaverMock measureSaver= new MeasureSaverMock();
    private TestResultsSaver saver;
    private TestResults projectSummaryResults = new TestResults();

    @Before
    public void before() {
        saver = new TestResultsSaver(measureSaver.getMock());      
    }
    
    @Test
    public void create_ExpectNotNull() {
        assertNotNull(saver);
    }
    
    @Test
    public void saveProjectResults_ShouldSave() {
        int errorredTests=5;
        
        projectSummaryResults.setErroredTests(errorredTests);
        int executedTests=4;
        projectSummaryResults.setExecutedTests(executedTests);
        int failedTests=3;
        projectSummaryResults.setFailedTests(failedTests);
        int passedTests=2;
        projectSummaryResults.setPassedTests(passedTests);
 
        saver.saveProjectTestResults(projectSummaryResults);
        
        measureSaver.verifySaveProjectSummary(CoreMetrics.TESTS,executedTests);
        measureSaver.verifySaveProjectSummary(CoreMetrics.TEST_FAILURES, failedTests);
        measureSaver.verifySaveProjectSummary(CoreMetrics.TEST_ERRORS, errorredTests);
        
    }
    
    @Test
    public void saveSummaryMeasures_ShouldSave() {
        UnitTestFileResultModelMock fileResults = new UnitTestFileResultModelMock();
        int failed=5;
        int density=20;
        int tests=4;
        
        fileResults.givenFailed(failed).givenDensity(density).givenTests(tests);
        ResourceSeamMock sonarFile = new ResourceSeamMock();
        saver.saveSummaryMeasures(fileResults.getMock(), sonarFile.getMock());
        
        sonarFile.verifySaveMetricValue(CoreMetrics.SKIPPED_TESTS,0);
        sonarFile.verifySaveMetricValue(CoreMetrics.TEST_ERRORS,0);
        sonarFile.verifySaveMetricValue(CoreMetrics.TEST_SUCCESS_DENSITY,density*100);
        sonarFile.verifySaveMetricValue(CoreMetrics.TEST_FAILURES,failed);
        sonarFile.verifySaveMetricValue(CoreMetrics.TESTS, tests);
    }
    
    @Test
    public void saveTestCaseMeasures_EmptyResults_MinimalDetail() {
        ClassUnitTestResult fileResults = new ClassUnitTestResult();
        ResourceSeamMock sonarFile = new ResourceSeamMock();
        saver.saveTestCaseMeasures(fileResults, sonarFile.getMock());
        String expected="<tests-details></tests-detailsX>";
        sonarFile.verifySaveMeasure(CoreMetrics.TEST_DATA,expected);
    }
}
