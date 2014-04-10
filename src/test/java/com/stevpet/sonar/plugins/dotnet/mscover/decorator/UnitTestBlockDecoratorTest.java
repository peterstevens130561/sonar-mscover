package com.stevpet.sonar.plugins.dotnet.mscover.decorator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class UnitTestBlockDecoratorTest {
    TimeMachine timeMachine;
    Settings settings;
    DecoratorContext context;
    @Before
    public void before() {
        timeMachine = mock(TimeMachine.class);
        settings = mock(Settings.class);
        context = mock(DecoratorContext.class);
    }
    
    @Test 
    public void createDecorator() {
        BaseDecorator decorator = new UnitTestLineDecorator(settings,timeMachine) ;
        Assert.assertNotNull(decorator);
    }
    
    @Test
    public void shouldExecute_Set_ExpectTrue() {
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_PATH)).thenReturn("a/b/c");
        BaseDecorator decorator = new UnitTestLineDecorator(settings,timeMachine) ;
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, settings);
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void shouldExecute_NotSet_ExpectFalse() {
        when(settings.getString(PropertiesHelper.MSCOVER_UNIT_PATH)).thenReturn(null);
        BaseDecorator decorator = new UnitTestLineDecorator(settings,timeMachine) ;
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, settings);
        Assert.assertFalse(shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        BaseDecorator decorator = new UnitTestLineDecorator(settings,timeMachine) ;  
        decorator.handleUncoveredResource(context, 4.0);
        verify(context,times(4)).saveMeasure(Matchers.any(Metric.class), Matchers.any(Double.class));
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        UnitTestLineDecorator decorator = new UnitTestLineDecorator(settings,timeMachine) ;  
        List<Metric> metrics = decorator.generatesCoverageMetrics();
        Assert.assertEquals(4,metrics.size());
    }
}
