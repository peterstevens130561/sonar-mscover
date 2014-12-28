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
    private UnitTestBlockDecorator  decorator;
    @Before
    public void before() {
        msCoverPropertiesMock.givenUnitTestsEnabled(false);
    }
    
    @Test 
    public void createDecorator() {
        createUnitTestBlockDecorator();
        Assert.assertNotNull(decorator);
    }

    @Test
    public void shouldExecute_Set_ExpectTrue() {
        createUnitTestBlockDecorator();
        msCoverPropertiesMock.givenUnitTestsEnabled(true);
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void shouldExecute_NotSet_ExpectFalse() {
        createUnitTestBlockDecorator();
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        Assert.assertFalse(shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        createUnitTestBlockDecorator();  
        decorator.handleUncoveredResource(decoratorContextMock.getMock(), 4.0);
        decoratorContextMock.verifySaveMeasureInvoked(3);
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        createUnitTestBlockDecorator();
        List<Metric> metrics = decorator.generatesCoverageMetrics();
        Assert.assertEquals(5,metrics.size());
    }
    
    private void createUnitTestBlockDecorator() {
        decorator = new UnitTestBlockDecorator(msCoverPropertiesMock.getMock(),timeMachineMock.getMock());
    }
}
