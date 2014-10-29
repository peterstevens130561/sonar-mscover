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
    public void runAnalysisForCSharpProjectTestsHaveNotRun_TestsShouldBeRun() {
        String resultsPath="results.path";
        String coveragePath="coverage.path";
        bd.givenANewSensor();
        testEnvironment.givenTestsHaveNotExecuted();
        bd.givenAnalysedProjectIsCSharpProject();
        bd.givenStubbedVsTestRunner();
        bd.givenTestResultsPath(resultsPath);
        bd.givenCoveragePath(coveragePath);
     
        bd.analyseProject();
        
        bd.verifyTestRunnerHasRun();
        testEnvironment.verifyTestsHaveRun();
        testEnvironment.verifyTestResultsPathIs(resultsPath);
        testEnvironment.verifyCoveragePathIs(coveragePath);
        
    }
    
    @Test
    public void runAnalysisForRootProjectTestsHaveNotRun_TestsShouldBeRun() {
        String resultsPath="results.path";
        String coveragePath="coverage.path";
        bd.givenANewSensor();
        testEnvironment.givenTestsHaveNotExecuted();
        bd.givenAnalysedProjectIsRootProject();
        bd.givenStubbedVsTestRunner();
        bd.givenTestResultsPath(resultsPath);
        bd.givenCoveragePath(coveragePath);
     
        bd.analyseProject();
        
        bd.verifyTestRunnerHasRun();
        testEnvironment.verifyTestsHaveRun();
        testEnvironment.verifyTestResultsPathIs(resultsPath);
        testEnvironment.verifyCoveragePathIs(coveragePath);
        
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
