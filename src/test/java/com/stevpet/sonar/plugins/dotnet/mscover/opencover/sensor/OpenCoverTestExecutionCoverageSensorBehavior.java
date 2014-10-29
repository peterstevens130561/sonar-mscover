package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverTestExecutionCoverageSensorBehavior {
    OpenCoverTestExecutionCoverageSensor sensor;
    private MsCoverProperties msCoverProperties;
    private VsTestEnvironmentMock vsTestEnvironment = new VsTestEnvironmentMock();
    private ModuleFileSystem moduleFileSystem = mock(ModuleFileSystem.class);
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment ;
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

    public void setProject(Project project) {
        this.project=project;
    }

    public void setMsCoverProperties(MsCoverProperties msCoverProperties) {
        this.msCoverProperties=msCoverProperties;
    }

    public void setMicrosoftWindowsEnvironment(MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
    }

    public void setOpenCoverCommand(OpenCoverCommand mock) {
        sensor.setOpenCoverCommand(mock);
    }

    public void analyse() {
        sensor.analyse(project, context);      
    };
}
