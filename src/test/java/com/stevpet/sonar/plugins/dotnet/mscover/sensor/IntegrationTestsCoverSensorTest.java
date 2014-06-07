package com.stevpet.sonar.plugins.dotnet.mscover.sensor;


import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.UnitTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyDouble;

public class IntegrationTestsCoverSensorTest {

    Settings settings ;
    Sensor sensor;
    Project project ;
    SensorContext context ;
    MicrosoftWindowsEnvironment microsoftWindowsEnvironment ;
    
    @Before
    public void before() {
        settings = mock(Settings.class);
        project = mock(Project.class);
        context = mock(SensorContext.class);
        microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
        sensor = new IntegrationTestsCoverSensorStub(settings,null,null);
        when(project.getFileSystem()).thenReturn( new DummyFileSystem());
        when(settings.getString(PropertiesHelper.MSCOVER_MODE)).thenReturn("reuse");
        
    }
    
    
    @Test
    public void IntegrationTestsSensor_PathNotSet_NotEnabled() {
        //Arrange
        when(settings.getString(anyString())).thenReturn(null);
        when(project.isRoot()).thenReturn(false);
        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        //Assert
        assertFalse(shouldExecute);         
    }
    
    @Test
    public void IntegrationTestsSensor_PathSet_Enabled() {
        //Arrange
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH)).thenReturn("a/b");
        when(project.isRoot()).thenReturn(false);
        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        //Assert
        assertTrue(shouldExecute); 

    }
    

    public void IntegrationTestsSensor_Analyse_Enabled() {
        //Arrange
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH)).thenReturn("mscover.xml");
        when(project.isRoot()).thenReturn(false);
        when(project.getName()).thenReturn("tfsblame");
        VisualStudioProject vsProject = mock(VisualStudioProject.class);
        File testProject=TestUtils.getResource("TfsBlame/tfsblame/tfsblame");
        when(vsProject.getDirectory()).thenReturn(testProject);
        when(microsoftWindowsEnvironment.getCurrentProject("tfsblame")).thenReturn(vsProject);
        //Act
        sensor.analyse(project, context);
        //Assert
        
    }
    

    
    public void UnitTestsSensor_Analyse_Enabled() {
        //Arrange
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH)).thenReturn("mscover.xml");
        when(project.isRoot()).thenReturn(false);
        when(project.getName()).thenReturn("tfsblame");
        VisualStudioProject vsProject = mock(VisualStudioProject.class);
        File testProject=TestUtils.getResource("TfsBlame/tfsblame/tfsblame");
        when(vsProject.getDirectory()).thenReturn(testProject);
        when(microsoftWindowsEnvironment.getCurrentProject("tfsblame")).thenReturn(vsProject);
        //Act
        sensor.analyse(project, context);
        //Assert
        
    }  
    
    @Test
    public void saveMeasure_Calls_ShouldBe5() {
        MeasureSaver measureSaver = mock(SonarMeasureSaver.class);
        LineMeasureSaver saver = UnitTestLineSaver.create(measureSaver);
        
        FileCoverage coverageData= mock(FileCoverage.class);
        when(coverageData.getCountLines()).thenReturn(10);
        when(coverageData.getCoveredLines()).thenReturn(6);
        when(coverageData.getUncoveredLines()).thenReturn(4);
        File file = mock(File.class);
        saver.saveMeasures(coverageData, file);
        //Assert
        verify(measureSaver,times(1)).saveMeasure(any(Measure.class));
        verify(measureSaver,times(5)).saveMeasure(any(Metric.class),anyDouble());
        
    }
    private class IntegrationTestsCoverSensorStub extends IntegrationTestCoverSensor {
        public  IntegrationTestsCoverSensorStub(Settings settings,
                MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
                TimeMachine timeMachine) {
            super(settings, microsoftWindowsEnvironment, timeMachine);
        }
    }
}
