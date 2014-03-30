package com.stevpet.sonar.plugins.dotnet.mscover.decorator;



import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.decorator.BaseDecorator;
import com.stevpet.sonar.plugins.dotnet.mscover.decorator.IntegrationTestDecorator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DecoratorShouldExecuteTest {

    @Test
    public void shouldExecute_PropertyNotSet_Disabled() {
        //Arrange
        Settings settings = mock(Settings.class);
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_PATH)).thenReturn(null);
        Decorator decorator = new CoverageDecoratorStub(settings);
        //Act
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Assert
        Assert.assertFalse(shouldExecute);
    }
    
    @Test
    public void shouldExecute_PropertySet_Enabled() {
        //Arrange
        Settings settings = mock(Settings.class);
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_PATH)).thenReturn("a/b/c");
        Decorator decorator = new CoverageDecoratorStub(settings);
        //Act
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Assert
        Assert.assertTrue(shouldExecute);
    }
    
    @Test 
    public void shouldExecuteIntegrationTests_PropertySet_Enabled() {
        //Arrange
        Settings settings = mock(Settings.class);
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_PATH)).thenReturn("a/b/c");
        Decorator decorate = new IntegrationTestDecorator(settings,null);
        //Act
        boolean actual = decorate.shouldExecuteOnProject(null);
        //Assert
        Assert.assertTrue(actual);
    }
    

 
}
