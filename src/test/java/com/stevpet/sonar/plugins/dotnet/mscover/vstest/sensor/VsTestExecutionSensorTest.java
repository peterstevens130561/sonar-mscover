package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.*;


import org.junit.Test;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import static org.mockito.Mockito.*;

public class VsTestExecutionSensorTest {

    VsTestExecutionSensor sensor;
    private MsCoverProperties msCoverProperties = mock(MsCoverProperties.class);
    private VsTestEnvironment vsTestEnvironment = mock(VsTestEnvironment.class);
    private ModuleFileSystem moduleFileSystem = mock(ModuleFileSystem.class);
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
 
    
    @Test
    public void testVsTestExecutionSensor() {
        givenANewTestExecutionSensor();
        verifyThatTheTestExecutionSensorExists();
    }

    private void verifyThatTheTestExecutionSensorExists() {
        assertNotNull(sensor);
    }

    private void givenANewTestExecutionSensor() {
        sensor=new VsTestExecutionSensor(vsTestEnvironment,msCoverProperties,moduleFileSystem,microsoftWindowsEnvironment);
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
