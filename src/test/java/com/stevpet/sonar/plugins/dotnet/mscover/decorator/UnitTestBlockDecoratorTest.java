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
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;

public class UnitTestBlockDecoratorTest {
    TimeMachine timeMachine;
    MsCoverPropertiesStub propertiesStub = new MsCoverPropertiesStub();
    DecoratorContext context;
    @Before
    public void before() {
        timeMachine = mock(TimeMachine.class);
        context = mock(DecoratorContext.class);
    }
    
    @Test 
    public void createDecorator() {
        BaseDecorator decorator = new UnitTestLineDecorator(propertiesStub,timeMachine) ;
        Assert.assertNotNull(decorator);
    }
    
    @Test
    public void shouldExecute_Set_ExpectTrue() {
        propertiesStub.setUnitTestsEnabled(true);
        BaseDecorator decorator = new UnitTestLineDecorator(propertiesStub,timeMachine) ;
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, propertiesStub);
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void shouldExecute_NotSet_ExpectFalse() {
        propertiesStub.setUnitTestsEnabled(false);
        BaseDecorator decorator = new UnitTestLineDecorator(propertiesStub,timeMachine) ;
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, propertiesStub);
        Assert.assertFalse(shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        BaseDecorator decorator = new UnitTestLineDecorator(propertiesStub,timeMachine) ;
        decorator.handleUncoveredResource(context, 4.0);
        verify(context,times(4)).saveMeasure(Matchers.any(Metric.class), Matchers.any(Double.class));
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        UnitTestLineDecorator decorator = new UnitTestLineDecorator(propertiesStub,timeMachine) ; ;  
        List<Metric> metrics = decorator.generatesCoverageMetrics();
        Assert.assertEquals(4,metrics.size());
    }
}
