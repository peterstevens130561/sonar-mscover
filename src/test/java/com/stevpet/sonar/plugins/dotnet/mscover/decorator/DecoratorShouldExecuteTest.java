package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.Decorator;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;

public class DecoratorShouldExecuteTest {

    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    @Test
    public void shouldExecute_PropertyNotSet_Disabled() {
        //Given
        msCoverPropertiesMock.givenIntegrationTestsEnabled(false);
     
        Decorator decorator = new CoverageDecoratorStub(msCoverPropertiesMock.getMock());
        //When
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Then
        Assert.assertFalse(shouldExecute);
    }
    
    @Test
    public void shouldExecute_PropertySet_Enabled() {
        //Given
        msCoverPropertiesMock.givenIntegrationTestsEnabled(true);
        Decorator decorator = new CoverageDecoratorStub(msCoverPropertiesMock.getMock());
        //When
        boolean shouldExecute = decorator.shouldExecuteOnProject(null);
        //Then
        Assert.assertTrue(shouldExecute);
    }
    
    @Test 
    public void shouldExecuteIntegrationTests_PropertySet_Enabled() {
        //Given
        msCoverPropertiesMock.givenIntegrationTestsEnabled(true);
        Decorator decorate = new IntegrationTestLineDecorator(msCoverPropertiesMock.getMock(),null);
        //When
        boolean actual = decorate.shouldExecuteOnProject(null);
        //Then
        Assert.assertTrue(actual);
    }
    

 
}
