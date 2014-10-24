package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

public class VsTestExecutionSensorTest {

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
 
    
    @Test
    public void testVsTestExecutionSensor() {
        givenANewTestExecutionSensor();
        verifyThatTheTestExecutionSensorExists();
    }
    
    @Test
    public void runAnalysisForEmptyProject() {
        givenANewTestExecutionSensor();
        givenTestsHaveExecuted();
        givenAnalysedProjectIsCSharpProject();
        givenStubbedVsTestRunner();
        
        analyseProject();
        
    }

    private void givenStubbedVsTestRunner() {
        when(vsTestRunnerFactory.createBasicTestRunnner(any(PropertiesHelper.class), 
                any(ModuleFileSystem.class), 
                any(MicrosoftWindowsEnvironment.class)
                )
                ).thenReturn(vsTestRunner);
    }

    private void analyseProject() {
        sensor.analyse(project, context);
    }

    private void givenAnalysedProjectIsCSharpProject() {
        when(project.isRoot()).thenReturn(false);
        when(moduleFileSystem.baseDir()).thenReturn(new File("mysolution/myproject"));
    }

    private void givenTestsHaveExecuted() {
    }

    private void verifyThatTheTestExecutionSensorExists() {
        assertNotNull(sensor);
    }

    private void givenANewTestExecutionSensor() {
        sensor=new VsTestExecutionSensor(vsTestEnvironment,msCoverProperties,moduleFileSystem,microsoftWindowsEnvironment);
        sensor.setVsTestRunnerFactory(vsTestRunnerFactory);
    }

    @Test
    public void testShouldExecuteOnProject() {
        fail("Not yet implemented");
    }

    @Test
    public void testAnalyse() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetVsTestRunnerFactory() {
        fail("Not yet implemented");
    }

}
