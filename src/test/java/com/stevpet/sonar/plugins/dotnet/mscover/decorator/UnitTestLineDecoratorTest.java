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

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class UnitTestLineDecoratorTest {
    TimeMachine timeMachine;
    Settings settings;
    MsCoverPropertiesStub propertiesHelper;
    DecoratorContext context;
    @Before
    public void before() {
        timeMachine = mock(TimeMachine.class);
        settings = mock(Settings.class);
        context = mock(DecoratorContext.class);
        propertiesHelper = new MsCoverPropertiesStub();
        propertiesHelper.setUnitTestsEnabled(false);
    }
    
    @Test 
    public void createDecorator() {
        BaseDecorator decorator = new UnitTestBlockDecorator(propertiesHelper,timeMachine) ;
        Assert.assertNotNull(decorator);
    }
    
    @Test
    public void shouldExecute_Set_ExpectTrue() {
        propertiesHelper.setUnitTestsEnabled(true);
        BaseDecorator decorator = new UnitTestBlockDecorator(propertiesHelper,timeMachine) ;
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, propertiesHelper);
        Assert.assertTrue(shouldExecute);
    }
    
    @Test
    public void shouldExecute_NotSet_ExpectFalse() {

        BaseDecorator decorator = new UnitTestBlockDecorator(propertiesHelper,timeMachine) ;
        boolean shouldExecute = decorator.shouldExecuteDecorator(null, propertiesHelper);
        Assert.assertFalse(shouldExecute);
    }
    
    @Test 
    public void handleUncoveredResource_ShouldSaveMeasures() {
        BaseDecorator decorator = new UnitTestBlockDecorator(propertiesHelper,timeMachine) ;  
        decorator.handleUncoveredResource(context, 4.0);
        verify(context,times(3)).saveMeasure(Matchers.any(Metric.class), Matchers.any(Double.class));
    }
    
    @Test
    public void generatesCoverageMetrics_ShouldHaveAll() {
        UnitTestBlockDecorator decorator = new UnitTestBlockDecorator(propertiesHelper,timeMachine) ;  
        List<Metric> metrics = decorator.generatesCoverageMetrics();
        Assert.assertEquals(5,metrics.size());
    }
}
