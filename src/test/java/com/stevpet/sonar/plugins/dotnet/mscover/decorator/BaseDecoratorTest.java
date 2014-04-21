package com.stevpet.sonar.plugins.dotnet.mscover.decorator;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseDecoratorTest {
    private Settings settings;
    private Resource<?> resource;
    private DecoratorContext context;
    @Before
    public void before() {
       settings = mock(Settings.class) ;
       resource = mock(Resource.class) ;
       context = mock(DecoratorContext.class);
    }
    
    
    @Test
    public void sunnyDay() {
        PrimitiveDecorator decorator = new PrimitiveDecorator(settings, null);
        when(resource.getScope()).thenReturn(Scopes.FILE);
        setNcLoc(10.0);
        setStatements(20.0);
        decorator.decorate(resource, context);
        Assert.assertTrue(decorator.isCalled());
    }
    
    @Test
    public void NoStatements_NotCalled() {
        PrimitiveDecorator decorator = new PrimitiveDecorator(settings, null);
        when(resource.getScope()).thenReturn(Scopes.FILE);
        setNcLoc(10.0);
        setStatements(0.0);
        decorator.decorate(resource, context);
        Assert.assertFalse(decorator.isCalled());
    }

    @Test
    public void NcLocUndefined_NotCalled() {
        PrimitiveDecorator decorator = new PrimitiveDecorator(settings, null);
        when(resource.getScope()).thenReturn(Scopes.FILE);
        setStatements(0.0);
        decorator.decorate(resource, context);
        Assert.assertFalse(decorator.isCalled());
    }
    
    @Test
    public void StatementsUndefined_NotCalled() {
        PrimitiveDecorator decorator = new PrimitiveDecorator(settings, null);
        when(resource.getScope()).thenReturn(Scopes.FILE);
        setNcLoc(10.0);
        decorator.decorate(resource, context);
        Assert.assertFalse(decorator.isCalled());
    }

    private void setNcLoc(double value) {
        Measure ncloc = new Measure(CoreMetrics.NCLOC);
        ncloc.setValue(value);
        when(context.getMeasure(CoreMetrics.NCLOC)).thenReturn(ncloc);
    }
    
    private void setStatements(double value) {
        Measure statements = new Measure(CoreMetrics.STATEMENTS);
        statements.setValue(value);
        when(context.getMeasure(CoreMetrics.STATEMENTS)).thenReturn(statements);          
    }
    
    class PrimitiveDecorator extends BaseDecorator {

        private boolean called=false;
        
        protected PrimitiveDecorator(Settings settings, TimeMachine timeMachine) {
            super(settings, timeMachine);
        }

        @Override
        public boolean shouldExecuteDecorator(Project project, Settings settings) {

            return false;
        }

        @Override
        protected void handleUncoveredResource(DecoratorContext context,
                double lines) {
            setCalled(true);
        }

        public boolean isCalled() {
            return called;
        }

        public void setCalled(boolean called) {
            this.called = called;
        }
        
    }
}
