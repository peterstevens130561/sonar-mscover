package com.stevpet.sonar.plugins.dotnet.mscover.sensor;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;
import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;
public class UnitTestsCoverSensorTest {

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
        when(project.getFileSystem()).thenReturn(new DummyFileSystem());
        sensor = new UnitTestCoverSensor(settings,null,null);
    }
    
    
    @Test
    public void UnitTestsSensor_PathNotSet_NotEnabled() {
        //Arrange
        when(settings.getString(anyString())).thenReturn(null);
        when(project.isRoot()).thenReturn(false);
        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        //Assert
        assertFalse(shouldExecute);         
    }
    
    @Test
    public void UnitTestsSensor_PathSet_Enabled() {
        //Arrange
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH)).thenReturn("a/b");
        when(project.isRoot()).thenReturn(false);

        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(project);
        //Assert
        assertTrue(shouldExecute);         
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
    public void UnitTestsSensor_GetSensorPath() {
        UnitTestSensorInspector inspector = new UnitTestSensorInspector(settings);
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH)).thenReturn("mscover.xml");
        String path = inspector.getCoveragePath();
        Assert.assertNotNull(path);
    }
    
    @Test
    public void unitTestsSensor_CreateLineSaver_ShouldBeSaver() {
        UnitTestSensorInspector inspector = new UnitTestSensorInspector(settings);
        Saver saver = inspector.createLineSaver(project, null,null);
        Assert.assertNotNull(saver);
    }
    
    @Test
    public void unitTestsSensor_CreateBlockSaver_ShouldBeBlockSaver() {
        UnitTestSensorInspector inspector = new UnitTestSensorInspector(settings);
        Saver saver = inspector.createBlockSaver(project, null);
        Assert.assertNotNull(saver);      
    }
    
    private class UnitTestSensorInspector extends UnitTestCoverSensor {

        public UnitTestSensorInspector(Settings settings) {
            super(settings,null,null);
        }    
    }
    
    
}
