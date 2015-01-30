package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.Decorator;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;

public class DecoratorShouldExecuteTest {

    MsCoverPropertiesMock settingsMock = new MsCoverPropertiesMock();
    @Test
    public void shouldExecute_PropertyNotSet_Disabled() {
        //Given
        settingsMock.givenIntegrationTestsPath(null);
     
        Decorator decorator = new CoverageDecoratorStub(settingsMock.getMock());
        //When
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Then
        Assert.assertFalse(shouldExecute);
    }
    
    @Test
    public void shouldExecute_PropertySet_Enabled() {
        //Given
        settingsMock.givenIntegrationTestsPath("a/b/c");
        Decorator decorator = new CoverageDecoratorStub(settingsMock.getMock());
        //When
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Then
        Assert.assertTrue(shouldExecute);
    }
    
    @Test 
    public void shouldExecuteIntegrationTests_PropertySet_Enabled() {
        //Given
        settingsMock.givenIntegrationTestsPath("a/b/c");
        Decorator decorate = new IntegrationTestLineDecorator(settingsMock.getMock(),null);
        //When
        boolean actual = decorate.shouldExecuteOnProject(null);
        //Then
        Assert.assertTrue(actual);
    }
    

 
}
