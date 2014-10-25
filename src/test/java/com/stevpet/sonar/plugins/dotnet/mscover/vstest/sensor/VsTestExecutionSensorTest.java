package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.*;

import org.junit.Test;


public class VsTestExecutionSensorTest  {
    private VsTestExecutionSensorBehaviour bd = new VsTestExecutionSensorBehaviour();
    @Test
    public void testVsTestExecutionSensor() {
        bd.givenANewTestExecutionSensor();
        bd.verifyThatTheTestExecutionSensorExists();
    }
    
    @Test
    public void runAnalysisForCSharpProjectTestsHaveNotRun_TestsShouldBeRun() {
        String resultsPath="results.path";
        String coveragePath="coverage.path";
        bd.givenANewTestExecutionSensor();
        bd.givenTestsHaveNotExecuted();
        bd.givenAnalysedProjectIsCSharpProject();
        bd.givenStubbedVsTestRunner();
        bd.givenTestResultsPath(resultsPath);
        bd.givenCoveragePath(coveragePath);
     
        bd.analyseProject();
        
        bd.verifyTestsHaveRun();
        bd.verifyTestResultsPathIs(resultsPath);
        bd.verifyCoveragePathIs(coveragePath);
        
    }
    
    @Test
    public void runAnalysisForRootProjectTestsHaveNotRun_TestsShouldBeRun() {
        String resultsPath="results.path";
        String coveragePath="coverage.path";
        bd.givenANewTestExecutionSensor();
        bd.givenTestsHaveNotExecuted();
        bd.givenAnalysedProjectIsRootProject();
        bd.givenStubbedVsTestRunner();
        bd.givenTestResultsPath(resultsPath);
        bd.givenCoveragePath(coveragePath);
     
        bd.analyseProject();
        
        bd.verifyTestsHaveRun();
        bd.verifyTestResultsPathIs(resultsPath);
        bd.verifyCoveragePathIs(coveragePath);
        
    }
    
    @Test
    public void runAnalysisForCSharpProjectTestsHaveRun_TestsShouldNotBeRun() {
        String resultsPath="results.path";
        String coveragePath="coverage.path";
        bd.givenANewTestExecutionSensor();
        bd.givenTestsHaveExecuted();
        bd.givenAnalysedProjectIsCSharpProject();
        bd.givenStubbedVsTestRunner();
        bd.givenTestResultsPath(resultsPath);
        bd.givenCoveragePath(coveragePath);
     
        bd.analyseProject();
        
        bd.verifyTestsHaveNotRun();
        bd.verifyTestEnvironmentPathsNotSet();
        bd.verifyTestRunnerPathsNotRequested();
    }

    @Test
    public void testRunPropertyNotSet_ShouldNotRun() {
        bd.givenRunVsTestPropertyNotSet();
        bd.givenANewTestExecutionSensor();
        bd.verifySensorShouldNotRun();
    }
    
    @Test
    public void testRunPropertySet_ShouldRun() {
        bd.givenRunVsTestPropertySet();
        bd.givenANewTestExecutionSensor();
        bd.verifySensorShouldRun();       
    }

}
