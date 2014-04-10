package com.stevpet.sonar.plugins.dotnet.mscover.sensor;


import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.test.TestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoverSensorTest {

    @Test
    public void happyDay_ShouldPass() {
        //Arrange
        Settings settings = mock(Settings.class);
        MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
        TimeMachine timeMachine = mock(TimeMachine.class);
        TestCoverSensor sensor = new TestCoverSensor(settings,microsoftWindowsEnvironment,timeMachine);
        Project project = mock(Project.class);
        when(project.getName()).thenReturn("tfsblame");
        ProjectFileSystem fileSystem = mock(ProjectFileSystem.class);
        when(fileSystem.getBasedir()).thenReturn(TestUtils.getResource("/"));
        when(project.getFileSystem()).thenReturn(fileSystem);
        SensorContext sensorContext = mock(SensorContext.class);
        //Act
        sensor.analyse(project, sensorContext);      
        //Assert
        TestsCoverageLineSaver saver = (TestsCoverageLineSaver)sensor.getCoverageSaver();
        Assert.assertEquals(0, saver.getHitDataCalls());
        Assert.assertEquals(0, saver.getSaveSummaryMeasuresCalls());
    }
    
}
