package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.Sensor;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractCoverSensorTest {
    private Settings settings;
    private Project project;
    private Sensor sensor;
    private PropertiesHelper helper;
    @Before
    public void before() {       

        project = mock(Project.class);
        settings= mock(Settings.class);
        sensor= new TestCoverSensor(settings,null,null);
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
        when(settings.getBoolean(PropertiesHelper.MSCOVER_EXECUTEROOT)).thenReturn(executeRoot);
        return sensor.shouldExecuteOnProject(project);          
    }
    
}
