package com.stevpet.sonar.plugins.dotnet.mscover.decorator;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;

public class IntegrationTestLineDecoratorTest {
    TimeMachineMock timeMachineMock = new TimeMachineMock();
    Settings settings;
    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    MsCoverPropertiesStub propertiesStub = new MsCoverPropertiesStub();
    DecoratorContextMock decoratorContextMock = new DecoratorContextMock();
    private BaseDecorator decorator ;
    @Before
    public void before() {
        settings = mock(Settings.class);
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
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        createDecorator();
        IntegrationTestLineDecorator lineDecorator = (IntegrationTestLineDecorator) decorator; 
        List<Metric> metrics = lineDecorator.generatesCoverageMetrics();
        Assert.assertEquals(4,metrics.size());
    }
    
    private void createDecorator() {
        decorator = new IntegrationTestLineDecorator(settings,timeMachineMock.getMock()) ;
    }
}
