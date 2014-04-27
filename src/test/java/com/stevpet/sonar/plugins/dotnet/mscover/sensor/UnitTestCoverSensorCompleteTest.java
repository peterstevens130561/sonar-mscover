package com.stevpet.sonar.plugins.dotnet.mscover.sensor;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.test.TestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.dotnet.mscover.sensor.results.ResultsSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunnerTestUtils;

public class UnitTestCoverSensorCompleteTest {
    private Project project;
    private SensorContext context;

    @Before 
    public void before() {
        context = mock(SensorContext.class);
        project = mock(Project.class);
        DummyFileSystem fileSystem = new DummyFileSystem();
        fileSystem.setBasedir(TestUtils.getResource("Mileage"));
        when(project.getFileSystem()).thenReturn( fileSystem);
    }
    @Test
    public void runTests_Analyse() {
        Settings settings = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        ResultsSensor sensor = new ResultsSensor(settings, null);
        sensor.analyse(project, context);
    }
    
    @Test
    public void runTests_ShouldExecute() {
        Settings settings = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        ResultsSensor sensor = new ResultsSensor(settings, null);
        when(project.isRoot()).thenReturn(true);
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void runTestsNotRoot_ShouldNotExecute() {
        Settings settings = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        ResultsSensor sensor = new ResultsSensor(settings, null);
        when(project.isRoot()).thenReturn(false);
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        Assert.assertFalse(shouldExecute);
    }
}
