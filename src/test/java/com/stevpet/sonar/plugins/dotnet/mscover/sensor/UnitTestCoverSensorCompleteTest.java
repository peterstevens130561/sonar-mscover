package com.stevpet.sonar.plugins.dotnet.mscover.sensor;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunnerTestUtils;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsSensor;

public class UnitTestCoverSensorCompleteTest {
    private Project project;
    private VsTestEnvironment vsTestEnvironment;

    @Before 
    public void before() {
        project = mock(Project.class);
        DummyFileSystem fileSystem = new DummyFileSystem();
        fileSystem.setBasedir(TestUtils.getResource("Mileage"));
        when(project.getFileSystem()).thenReturn( fileSystem);
    }


    @Test
    public void projectIsRootPropertyExecuteRootIsTrue_ShouldNotExecute() {
        Settings settings = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        VsTestUnitTestResultsSensor sensor = new VsTestUnitTestResultsSensor(null,settings, null,vsTestEnvironment);
        when(project.isRoot()).thenReturn(true);
        
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        Assert.assertFalse(shouldExecute);
    }
    
    @Test
    public void runTestsNotRoot_ShouldExecute() {
        Settings settings = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        VsTestUnitTestResultsSensor sensor = new VsTestUnitTestResultsSensor(null,settings, null,vsTestEnvironment);
        when(project.isRoot()).thenReturn(false);
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        Assert.assertTrue(shouldExecute);
    }
}
