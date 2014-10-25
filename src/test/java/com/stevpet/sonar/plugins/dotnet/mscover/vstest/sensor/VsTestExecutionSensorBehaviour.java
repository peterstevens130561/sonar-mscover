package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

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
    private MsCoverProperties propertiesHelper;

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

    protected void givenTestsHaveExecuted() {
    }

    protected void verifyThatTheTestExecutionSensorExists() {
        assertNotNull(sensor);
    }

    protected void givenANewTestExecutionSensor() {
        sensor=new VsTestExecutionSensor(vsTestEnvironment,msCoverProperties,moduleFileSystem,microsoftWindowsEnvironment);
        sensor.setVsTestRunnerFactory(vsTestRunnerFactory);
    }

}