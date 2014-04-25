package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

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
    public void runTests() {
        Settings settings = UnitTestRunnerTestUtils.mockUnitTestRunnerSettingsToRun();
        ResultsSensor sensor = new ResultsSensor(settings);
        sensor.analyse(project, context);
    }
}
