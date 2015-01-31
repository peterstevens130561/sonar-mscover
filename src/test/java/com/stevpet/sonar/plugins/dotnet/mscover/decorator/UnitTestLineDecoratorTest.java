package com.stevpet.sonar.plugins.dotnet.mscover.decorator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.SettingsMock;

public class UnitTestLineDecoratorTest {
    TimeMachineMock timeMachineMock = new TimeMachineMock();
    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    DecoratorContextMock decoratorContextMock = new DecoratorContextMock();
    SettingsMock settingsMock = new SettingsMock();
    UnitTestLineDecorator decorator;

    @Before
    public void before() {
        createUnitTestLineDecorator();        
    }
    
    @Test 
    public void createDecorator() {
        Assert.assertNotNull(decorator);
    }

    private void createUnitTestLineDecorator() {
        decorator = new UnitTestLineDecorator(msCoverPropertiesMock.getMock(),timeMachineMock.getMock()) ;
    }
    
    @Test
    public void shouldExecute_Set_ExpectTrue() {
        msCoverPropertiesMock.givenUnitTestsEnabled(true);
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        Assert.assertTrue("unitTestLineDecorator shouldExecute",shouldExecute);
    }
    
    @Test
    public void shouldExecute_NotSet_ExpectFalse() {
        msCoverPropertiesMock.givenUnitTestsEnabled(false);
        createUnitTestLineDecorator();
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        Assert.assertFalse("unitTestLineDecorator shouldExecute",shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        decorator.handleUncoveredResource(decoratorContextMock.getMock(), 4.0);
        decoratorContextMock.verifySaveMeasureInvoked(4);
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        List<Metric> metrics = decorator.generatesCoverageMetrics();
        Assert.assertEquals(4,metrics.size());
    }
}
