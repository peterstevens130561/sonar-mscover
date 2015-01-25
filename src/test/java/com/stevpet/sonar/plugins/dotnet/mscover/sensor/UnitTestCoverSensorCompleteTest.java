package com.stevpet.sonar.plugins.dotnet.mscover.sensor;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunnerTestUtils;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsSensor;

public class UnitTestCoverSensorCompleteTest {
    private Project project;
    private VsTestEnvironment vsTestEnvironment;
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();

    @Before 
    public void before() {
        project = mock(Project.class);
    }


    @Test
    public void projectIsRootPropertyExecuteRootIsTrue_ShouldNotExecute() {
        MsCoverProperties propertiesHelper = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        VsTestUnitTestResultsSensor sensor = createSensor(propertiesHelper);
        when(project.isRoot()).thenReturn(true);
        
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        Assert.assertFalse(shouldExecute);
    }


    private VsTestUnitTestResultsSensor createSensor(
            MsCoverProperties propertiesHelper) {
        VsTestUnitTestResultsSensor sensor = new VsTestUnitTestResultsSensor(propertiesHelper, null,vsTestEnvironment,null,microsoftWindowsEnvironmentMock.getMock());
        return sensor;
    }
    
    @Test
    public void runTestsNotRoot_ShouldExecute() {
        MsCoverProperties propertiesHelper = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        VsTestUnitTestResultsSensor sensor = createSensor(propertiesHelper);
        when(project.isRoot()).thenReturn(false);
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        Assert.assertTrue(shouldExecute);
    }
}
