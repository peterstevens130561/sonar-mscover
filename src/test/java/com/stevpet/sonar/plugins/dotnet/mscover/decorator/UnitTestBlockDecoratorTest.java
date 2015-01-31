package com.stevpet.sonar.plugins.dotnet.mscover.decorator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
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
    private UnitTestBlockDecorator  decorator;
    @Before
    public void before() {
        msCoverPropertiesMock.givenUnitTestsEnabled(false);
        createUnitTestBlockDecorator();
    }
    
    @Test 
    public void createDecorator() {
        Assert.assertNotNull(decorator);
    }

    @Test
    public void shouldExecute_Set_ExpectTrue() {
        msCoverPropertiesMock.givenUnitTestsEnabled(true);
        boolean shouldExecute = whenInvokeShouldExecute();
        Assert.assertTrue("decorator shouldExecute",shouldExecute);
    }


    
    @Test
    public void shouldExecute_NotSet_ExpectFalse() {
        msCoverPropertiesMock.givenUnitTestsEnabled(false);
        boolean shouldExecute = whenInvokeShouldExecute();
        Assert.assertFalse("decorator shouldExecute",shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        decorator.handleUncoveredResource(decoratorContextMock.getMock(), 4.0);
        decoratorContextMock.verifySaveMeasureInvoked(3);
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        List<Metric> metrics = decorator.generatesCoverageMetrics();
        Assert.assertEquals(5,metrics.size());
    }
    
    private void createUnitTestBlockDecorator() {
        decorator = new UnitTestBlockDecorator(msCoverPropertiesMock.getMock(),timeMachineMock.getMock());
    }
    
    private boolean whenInvokeShouldExecute() {
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        return shouldExecute;
    }
}
