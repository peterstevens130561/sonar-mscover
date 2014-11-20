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

public class IntegrationTestCoverSensorTest {
    private MsCoverProperties propertiesHelper;
    private Project project;
    private Sensor sensor;
    @Before
    public void before() {       

        project = mock(Project.class);
        propertiesHelper = mock(PropertiesHelper.class);
        sensor= new IntegrationTestCoverSensor(propertiesHelper,null,null);
        when(propertiesHelper.getMode()).thenReturn("reuse");
        when(propertiesHelper.getIntegrationTestsPath()).thenReturn("hi");
        when(propertiesHelper.isIntegrationTestsEnabled()).thenReturn(true);
        //when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH)).thenReturn("hi");
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
    public void ShouldExecute_IsRootDoNotExecuteRoot_False() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(true,false);        
        Assert.assertFalse(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_IsNotRootDoExecuteRoot_False() {
        boolean shouldExecute=arrangeAndActisRootExecuteRoot(false,true);      
        Assert.assertFalse(shouldExecute);
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
