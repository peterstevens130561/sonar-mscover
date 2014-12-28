package com.stevpet.sonar.plugins.dotnet.mscover.decorator;


import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.SettingsMock;

public class IntegrationTestLineDecoratorTest {
    TimeMachineMock timeMachineMock = new TimeMachineMock();
    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    MsCoverPropertiesStub propertiesStub = new MsCoverPropertiesStub();
    DecoratorContextMock decoratorContextMock = new DecoratorContextMock();
    private BaseDecorator decorator ;
    SettingsMock settingsMock = new SettingsMock();
    @Before
    public void before() {
    }
    
    @Test 
    public void createDecoratorTest_shouldBeCreated() {
        createDecorator();
        Assert.assertNotNull(decorator);
    }

    @Test
    public void shouldExecute_Set_ExpectTrue() {
        msCoverPropertiesMock.givenIntegrationTestsPath("a/b/c");
        msCoverPropertiesMock.givenIntegrationTestsEnabled(true);
        createDecorator();
       
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void shouldExecute_NotSet_ExpectFalse() {
        msCoverPropertiesMock.givenIntegrationTestsPath(null);
        createDecorator();
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, msCoverPropertiesMock.getMock());
        Assert.assertFalse(shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        createDecorator();  
        decorator.handleUncoveredResource(decoratorContextMock.getMock(), 4.0);
        decoratorContextMock.verifySaveMeasureInvoked(4);

    }
    
    /**
     * just verify that there are 4 metrics generated.
     */
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        createDecorator();
        IntegrationTestLineDecorator lineDecorator = (IntegrationTestLineDecorator) decorator; 
        List<Metric> metrics = lineDecorator.generatesCoverageMetrics();
        Assert.assertEquals(4,metrics.size());
    }
    
    private void createDecorator() {
        decorator = new IntegrationTestLineDecorator(settingsMock.getMock(),timeMachineMock.getMock()) ;
    }
}
