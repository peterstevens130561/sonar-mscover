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
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IntegrationTestBlockDecoratorTest {
    TimeMachine timeMachine;
    Settings settings;
    MsCoverProperties propertiesHelper;
    DecoratorContext context;
    @Before
    public void before() {
        timeMachine = mock(TimeMachine.class);
        propertiesHelper = mock(PropertiesHelper.class);
        context = mock(DecoratorContext.class);
    }
    
    @Test 
    public void CreateDecorator() {
        BaseDecorator decorator = new IntegrationTestBlockDecorator(settings,timeMachine) ;
        Assert.assertNotNull(decorator);
    }
    
    @Test
    public void ShouldExecute_Set_ExpectTrue() {
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH)).thenReturn("a/b/c");
        BaseDecorator decorator = new IntegrationTestBlockDecorator(settings,timeMachine) ;
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, propertiesHelper);
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void ShouldExecute_NotSet_ExpectFalse() {
        when(settings.getString(PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH)).thenReturn(null);
        BaseDecorator decorator = new IntegrationTestBlockDecorator(settings,timeMachine) ;
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, propertiesHelper);
        Assert.assertFalse(shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        BaseDecorator decorator = new IntegrationTestBlockDecorator(settings,timeMachine) ;  
        decorator.handleUncoveredResource(context, 4.0);
        verify(context,times(3)).saveMeasure(Matchers.any(Metric.class), Matchers.any(Double.class));
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        IntegrationTestBlockDecorator decorator = new IntegrationTestBlockDecorator(settings,timeMachine) ;  
        List<Metric> metrics = decorator.generatesCoverageMetrics();
        Assert.assertEquals(5,metrics.size());
    }
}
