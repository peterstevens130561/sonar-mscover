package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Assert;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

public class VsTestExecutionSensorBehaviour {

    VsTestExecutionSensor sensor;
    private VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    private MsCoverProperties msCoverProperties = mock(MsCoverProperties.class);
    public VsTestEnvironment vsTestEnvironment = mock(VsTestEnvironment.class);
    private FileSystem fileSystem = mock(FileSystem.class);
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
    private Project project = mock(Project.class);
    private SensorContext context = mock(SensorContext.class);
    private AbstractVsTestRunnerFactory vsTestRunnerFactory = mock(AbstractVsTestRunnerFactory.class);
    private VsTestRunner vsTestRunner = mock(VsTestRunner.class);


    protected void givenStubbedVsTestRunner() {
        when(vsTestRunnerFactory.createBasicTestRunnner(any(PropertiesHelper.class), 
                any(FileSystem.class), 
                any(MicrosoftWindowsEnvironment.class)
                )
                ).thenReturn(vsTestRunner);
    }

    protected void analyseProject() {
        sensor.analyse(project, context);
    }

    protected void givenAnalysedProjectIsCSharpProject() {
        when(project.isRoot()).thenReturn(false);
        when(fileSystem.baseDir()).thenReturn(new File("mysolution/myproject"));
    }
    
    public void givenAnalysedProjectIsRootProject() {
        when(project.isRoot()).thenReturn(true);
    }

    /**
     * @deprecated Use {@link com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock#givenTestsHaveExecuted(com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensorBehaviour)} instead
     */
    protected void givenTestsHaveExecuted() {
        vsTestEnvironmentMock.givenTestsHaveExecuted();
    }

    /**
     * @deprecated Use {@link com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock#givenTestsHaveNotExecuted(com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensorBehaviour)} instead
     */
    public void givenTestsHaveNotExecuted() {
        vsTestEnvironmentMock.givenTestsHaveNotExecuted();
    }
    
    protected void verifyThatTheTestExecutionSensorExists() {
        assertNotNull(sensor);
    }

    protected void givenANewSensor() {
        sensor=new VsTestExecutionSensor(vsTestEnvironmentMock.getMock(),msCoverProperties,fileSystem,microsoftWindowsEnvironment);
        sensor.setVsTestRunnerFactory(vsTestRunnerFactory);
    }

    public void givenTestResultsPath(String resultsPath) {
        when(vsTestRunner.getResultsXmlPath()).thenReturn(resultsPath);
    }

    public void verifyTestRunnerHasRun() {
        verify(vsTestRunner,times(1)).runTests();
    }
    public void verifyTestsHaveNotRun() {
        verify(vsTestEnvironment,times(0)).setTestsHaveRun();
        verify(vsTestRunner,times(0)).runTests();
    }
    /**
     * @deprecated Use {@link com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock#verifyTestResultsPathIs(com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensorBehaviour,String)} instead
     */
    public void verifyTestResultsPathIs(String resultsPath) {
        vsTestEnvironmentMock.verifyTestResultsPathIs(resultsPath);
    }

    /**
     * @deprecated Use {@link com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensor#verifyCoveragePathIs(com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensorBehaviour,String)} instead
     */
    public void verifyCoveragePathIs(String coveragePath) {
        vsTestEnvironmentMock.verifyCoveragePathIs(coveragePath);
    }

    public void givenCoveragePath(String coveragePath) {
        when(vsTestRunner.getCoverageXmlPath()).thenReturn(coveragePath);
    }

    /**
     * @deprecated Use {@link com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock#verifyTestEnvironmentPathsNotSet(com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensorBehaviour)} instead
     */
    public void verifyTestEnvironmentPathsNotSet() {
        vsTestEnvironmentMock.verifyTestEnvironmentPathsNotSet();
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

    public VsTestEnvironmentMock getTestEnvironment() {
        return vsTestEnvironmentMock;
    }




}