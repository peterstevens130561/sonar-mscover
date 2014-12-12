package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.Decorator;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.SettingsMock;

public class DecoratorShouldExecuteTest {

    SettingsMock settingsMock = new SettingsMock();
    @Test
    public void shouldExecute_PropertyNotSet_Disabled() {
        //Given
        settingsMock.givenString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH,null);
      
        Decorator decorator = new CoverageDecoratorStub(settingsMock.getMock());
        //When
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Then
        Assert.assertFalse(shouldExecute);
    }
    
    @Test
    public void shouldExecute_PropertySet_Enabled() {
        //Given
        settingsMock.givenString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH,"a/b/c");
        Decorator decorator = new CoverageDecoratorStub(settingsMock.getMock());
        //When
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Then
        Assert.assertTrue(shouldExecute);
    }
    
    @Test 
    public void shouldExecuteIntegrationTests_PropertySet_Enabled() {
        //Given
        settingsMock.givenString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH, "a/b/c");
        Decorator decorate = new IntegrationTestLineDecorator(settingsMock.getMock(),null);
        //When
        boolean actual = decorate.shouldExecuteOnProject(null);
        //Then
        Assert.assertTrue(actual);
    }
    

 
}
