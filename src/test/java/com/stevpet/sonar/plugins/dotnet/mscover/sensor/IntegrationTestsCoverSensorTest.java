package com.stevpet.sonar.plugins.dotnet.mscover.sensor;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.UnitTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestsCoverSensorTest {

    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    ResourceMediatorMock resourceMediatorMock = new ResourceMediatorMock();
    Sensor sensor;
    ProjectMock projectMock = new ProjectMock();
    SensorContext context ;
    MicrosoftWindowsEnvironment microsoftWindowsEnvironment ;
    private FileSystemMock fileSystemMock = new FileSystemMock();
    private CoverageHelperFactoryMock coverageHelperFactoryMock = new CoverageHelperFactoryMock();
    private ShouldExecuteHelperMock shouldExecuteHelperMock = new ShouldExecuteHelperMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    
    @Before
    public void before() {
        context = mock(SensorContext.class);
        coverageHelperFactoryMock.WhenCreateShouldExecuteHelper(shouldExecuteHelperMock);
        microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
        sensor = new IntegrationTestsCoverSensorStub(msCoverPropertiesMock.getMock(),microsoftWindowsEnvironmentMock.getMock(),null);
        msCoverPropertiesMock.givenMode("reuse");

        
    }
    
    
    @Test
    public void IntegrationTestsSensor_PathNotSet_NotEnabled() {
        //Arrange
        projectMock.givenIsRoot(false);

        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(projectMock.getMock());
        //Assert
        assertFalse(shouldExecute);         
    }
    
    @Test
    public void IntegrationTestsSensor_PathSet_Enabled() {
        //Arrange
        msCoverPropertiesMock.givenIntegrationTestsEnabled(true);
        shouldExecuteHelperMock.whenShouldExecute(projectMock.getMock(),true);
        projectMock.givenIsRoot(false);
        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(projectMock.getMock());
        //Assert
        assertTrue(shouldExecute); 
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
        verify(measureSaver,times(1)).saveFileMeasure(any(Measure.class));
        verify(measureSaver,times(5)).saveFileMeasure(any(Metric.class),anyDouble());
        
    }
    private class IntegrationTestsCoverSensorStub extends IntegrationTestCoverSensor {
        public  IntegrationTestsCoverSensorStub(MsCoverProperties propertiesHelper,
                MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
                TimeMachine timeMachine) {
            super(propertiesHelper, timeMachine,coverageHelperFactoryMock.getMock(),fileSystemMock.getMock(),microsoftWindowsEnvironment,resourceMediatorMock.getMock());
        }
    }
}
