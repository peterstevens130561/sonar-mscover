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


public class VsTestExecutionSensorTest  {

    private VsTestExecutionSensorBehaviour bd = new VsTestExecutionSensorBehaviour();
    private VsTestEnvironmentMock testEnvironment ;
    
    @Before
    public void before() {
        testEnvironment=bd.getTestEnvironment();
    }
    @Test
    public void testVsTestExecutionSensor() {
        bd.givenANewSensor();
        bd.verifyThatTheTestExecutionSensorExists();
    }
    
    @Test
    public void runAnalysisForCSharpProjectTestsHaveNotRun_TestsShouldNotBeRun() {
        String resultsPath="results.path";
        String coveragePath="coverage.path";
        bd.givenANewSensor();
        testEnvironment.givenTestsHaveNotExecuted();
        bd.givenAnalysedProjectIsCSharpProject();
        bd.givenStubbedVsTestRunner();
        bd.givenTestResultsPath(resultsPath);
        bd.givenCoveragePath(coveragePath);    
        bd.analyseProject();        
        bd.verifyTestsHaveNotRun();
        testEnvironment.verifyTestEnvironmentPathsNotSet();
        bd.verifyTestRunnerPathsNotRequested();
        
    }
    
    
    @Test
    public void runAnalysisForCSharpProjectTestsHaveRun_TestsShouldNotBeRun() {
        String resultsPath="results.path";
        String coveragePath="coverage.path";
        bd.givenANewSensor();
        testEnvironment.givenTestsHaveExecuted();
        bd.givenAnalysedProjectIsCSharpProject();
        bd.givenStubbedVsTestRunner();
        bd.givenTestResultsPath(resultsPath);
        bd.givenCoveragePath(coveragePath);
     
        bd.analyseProject();
        
        bd.verifyTestsHaveNotRun();
        testEnvironment.verifyTestEnvironmentPathsNotSet();
        bd.verifyTestRunnerPathsNotRequested();
    }

    @Test
    public void testRunPropertyNotSet_ShouldNotRun() {
        bd.givenRunVsTestPropertyNotSet();
        bd.givenANewSensor();
        bd.verifySensorShouldNotRun();
    }
    
    @Test
    public void testRunPropertySet_ShouldRun() {
        bd.givenRunVsTestPropertySet();
        bd.givenANewSensor();
        bd.verifySensorShouldRun();       
    }

}
