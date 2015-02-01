package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.Sensor;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;

public class IntegrationTestCoverSensorTest {
    private MsCoverProperties propertiesHelper;
    private Project project;
    private Sensor sensor;
    private SonarCoverageHelperFactory sonarCoverageHelperFactory = new SonarCoverageHelperFactory();
    private MicrosoftWindowsEnvironmentMock microsoftwWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private MeasureSaverMock measureSaverMock = new MeasureSaverMock();
    @Before
    public void before() {       

        project = mock(Project.class);
        propertiesHelper = mock(PropertiesHelper.class);
        sensor= new IntegrationTestCoverSensor(propertiesHelper,sonarCoverageHelperFactory,null,microsoftwWindowsEnvironmentMock.getMock(),measureSaverMock.getMock());
        when(propertiesHelper.getMode()).thenReturn("reuse");
        when(propertiesHelper.getIntegrationTestsPath()).thenReturn("hi");
        when(propertiesHelper.isIntegrationTestsEnabled()).thenReturn(true);
    }
    
    @Test
    public void ShouldExecute_NullProject_False() {
        //Arrange
        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(null);
        //Assert
        Assert.assertFalse(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_IsRootDoExecuteRoot_False() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(true,false);        
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_IsNotRootDoExecuteRoot_False() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(false,true);      
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_IsRootDoExecuteRoot_True() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(true,true);        
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_IsNotRootDoNotExecuteRoot_True() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(false,false);      
        Assert.assertTrue(shouldExecute);
    }
    boolean arrangeAndActisRootExecuteRoot(boolean isRoot, boolean executeRoot) {
        when(project.isRoot()).thenReturn(isRoot);  
        when(propertiesHelper.excuteRoot()).thenReturn(executeRoot);
        return sensor.shouldExecuteOnProject(project);          
    }
    
}
