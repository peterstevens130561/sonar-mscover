package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.sensor.AbstractDotNetSensor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
public class OpenCoverCoverageSensorTest {

    private AbstractDotNetSensor sensor;

    @Before
    public void before() {
        MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
        Settings settings = mock(Settings.class);
        sensor = new OpenCoverCoverageSensor(microsoftWindowsEnvironment,settings);
          
    }
    
    @Test
    public void createSensor_expect() {
       assertNotNull(sensor);
    }
    
    @Test
    public void testProject_ShouldNotExecute() {
        Project project = mock(Project.class);
        DotNetProject dp ;
        when(project.
    }
}
