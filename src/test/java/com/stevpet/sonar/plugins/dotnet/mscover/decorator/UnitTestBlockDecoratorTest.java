package com.stevpet.sonar.plugins.dotnet.mscover.decorator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.SettingsMock;

public class UnitTestBlockDecoratorTest {
    TimeMachineMock timeMachineMock = new TimeMachineMock();
    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    DecoratorContextMock decoratorContextMock = new DecoratorContextMock();
    SettingsMock settingsMock = new SettingsMock();
    UnitTestLineDecorator decorator;


    
    @Test 
    public void createDecorator() {
        createUnitTestLineDecorator();
        Assert.assertNotNull(decorator);
    }

    private void createUnitTestLineDecorator() {
        decorator = new UnitTestLineDecorator(msCoverPropertiesMock.getMock(),timeMachineMock.getMock()) ;
    }
    
    @Test
    public void shouldExecute_Set_ExpectTrue() {
        msCoverPropertiesMock.givenUnitTestsEnabled(true);
        createUnitTestLineDecorator();
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void shouldExecute_NotSet_ExpectFalse() {
        msCoverPropertiesMock.givenUnitTestsEnabled(false);
        createUnitTestLineDecorator();
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        Assert.assertFalse(shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        createDecorator();  
        decorator.handleUncoveredResource(decoratorContextMock.getMock(), 4.0);
        decoratorContextMock.verifySaveMeasureInvoked(4);
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        createUnitTestLineDecorator();
        List<Metric> metrics = decorator.generatesCoverageMetrics();
        Assert.assertEquals(4,metrics.size());
    }
}
