package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Assert;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

public class VsTestExecutionSensorBehaviour {

    VsTestExecutionSensor sensor;
    private MsCoverProperties msCoverProperties = mock(MsCoverProperties.class);
    private VsTestEnvironment vsTestEnvironment = mock(VsTestEnvironment.class);
    private ModuleFileSystem moduleFileSystem = mock(ModuleFileSystem.class);
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
    private Project project = mock(Project.class);
    private SensorContext context = mock(SensorContext.class);
    private AbstractVsTestRunnerFactory vsTestRunnerFactory = mock(AbstractVsTestRunnerFactory.class);
    private VsTestRunner vsTestRunner = mock(VsTestRunner.class);

    public VsTestExecutionSensorBehaviour() {
        super();
    }

    protected void givenStubbedVsTestRunner() {
        when(vsTestRunnerFactory.createBasicTestRunnner(any(PropertiesHelper.class), 
                any(ModuleFileSystem.class), 
                any(MicrosoftWindowsEnvironment.class)
                )
                ).thenReturn(vsTestRunner);
    }

    protected void analyseProject() {
        sensor.analyse(project, context);
    }

    protected void givenAnalysedProjectIsCSharpProject() {
        when(project.isRoot()).thenReturn(false);
        when(moduleFileSystem.baseDir()).thenReturn(new File("mysolution/myproject"));
    }
    
    public void givenAnalysedProjectIsRootProject() {
        when(project.isRoot()).thenReturn(true);
    }

    protected void givenTestsHaveExecuted() {
        when(vsTestEnvironment.getTestsHaveRun()).thenReturn(true);
    }

    public void givenTestsHaveNotExecuted() {
        when(vsTestEnvironment.getTestsHaveRun()).thenReturn(false);       // TODO Auto-generated method stub
        
    }
    protected void verifyThatTheTestExecutionSensorExists() {
        assertNotNull(sensor);
    }

    protected void givenANewTestExecutionSensor() {
        sensor=new VsTestExecutionSensor(vsTestEnvironment,msCoverProperties,moduleFileSystem,microsoftWindowsEnvironment);
        sensor.setVsTestRunnerFactory(vsTestRunnerFactory);
    }

    public void givenTestResultsPath(String resultsPath) {
        when(vsTestRunner.getResultsXmlPath()).thenReturn(resultsPath);
    }

    public void verifyTestsHaveRun() {
        verify(vsTestEnvironment,times(1)).setTestsHaveRun();
        verify(vsTestRunner,times(1)).runTests();
    }
    public void verifyTestsHaveNotRun() {
        verify(vsTestEnvironment,times(0)).setTestsHaveRun();
        verify(vsTestRunner,times(0)).runTests();
    }
    public void verifyTestResultsPathIs(String resultsPath) {
        verify(vsTestEnvironment,times(1)).setTestResultsXmlPath(resultsPath);
    }

    public void verifyCoveragePathIs(String coveragePath) {
        verify(vsTestEnvironment,times(1)).setCoverageXmlPath(coveragePath);
    }

    public void givenCoveragePath(String coveragePath) {
        when(vsTestRunner.getCoverageXmlPath()).thenReturn(coveragePath);
    }

    public void verifyTestEnvironmentPathsNotSet() {
        verify(vsTestEnvironment,times(0)).setCoverageXmlPath(anyString());
        verify(vsTestEnvironment,times(0)).setTestResultsXmlPath(anyString());
    }

    public void verifyTestRunnerPathsNotRequested() {
        verify(vsTestRunner,times(0)).getResultsXmlPath();
        verify(vsTestRunner,times(0)).getCoverageXmlPath();
    }

    public void givenRunVsTestPropertyNotSet() {
        when(msCoverProperties.runVsTest()).thenReturn(false);
    }

    public void givenRunVsTestPropertySet() {
        when(msCoverProperties.runVsTest()).thenReturn(true);
    }

    public void verifySensorShouldNotRun() {
        Assert.assertFalse(sensor.shouldExecuteOnProject(project));
    }

    public void verifySensorShouldRun() {
        boolean result=sensor.shouldExecuteOnProject(project);
        Assert.assertTrue(result);
    }




}