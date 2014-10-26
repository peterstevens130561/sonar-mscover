package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class OpenCoverTestExecutionCoverageSensorBehavior {
    OpenCoverTestExecutionCoverageSensor sensor;
    private MsCoverProperties msCoverProperties = mock(MsCoverProperties.class);
    private VsTestEnvironmentMock vsTestEnvironment = new VsTestEnvironmentMock();
    private ModuleFileSystem moduleFileSystem = mock(ModuleFileSystem.class);
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
    private Project project = mock(Project.class);
    private SensorContext context = mock(SensorContext.class);

    

    public void givenANewSensor() {
        sensor=new OpenCoverTestExecutionCoverageSensor(msCoverProperties, 
                microsoftWindowsEnvironment, 
                moduleFileSystem, 
                vsTestEnvironment.getMock());
    }

    public void verifyThatSensorExists() {
        assertNotNull(sensor);
    }
    
    public VsTestEnvironmentMock getVsTestEnvironmentMock() {
        return vsTestEnvironment;
    }

    public void verifyShouldAnalyseReturns(boolean expected) {
        boolean result=sensor.shouldExecuteOnProject(project);
        assertEquals(expected,result);
    }
}
