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

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.ResourceMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseDecoratorTest {
    private ResourceMock resourceMock = new ResourceMock();
    private DecoratorContextMock decoratorContextMock = new DecoratorContextMock();
    private PrimitiveDecorator decorator;
    private MsCoverPropertiesStub msCoverPropertiesStub;
    @Before
    public void before() {
       msCoverPropertiesStub = new MsCoverPropertiesStub();
       decorator = new PrimitiveDecorator(msCoverPropertiesStub, null);
    }
    
    
    @Test
    public void sunnyDay() {
        resourceMock.givenScope(Scopes.FILE);
        resourceMock.givenLongName("somename");
        givenNcLoc(10.0);
        givenStatements(20.0);
        createDecorator();
        Assert.assertTrue(decorator.isCalled());
    }

    private void createDecorator() {
        decorator.decorate(resourceMock.getMock(), decoratorContextMock.getMock());
    }
    
    @Test
    public void NoStatements_NotCalled() {
        resourceMock.givenScope(Scopes.FILE);
        givenNcLoc(10.0);
        givenStatements(0.0);
        createDecorator();
        Assert.assertFalse(decorator.isCalled());
    }

    @Test
    public void NcLocUndefined_NotCalled() {
        resourceMock.givenScope(Scopes.FILE);
        givenStatements(0.0);
        createDecorator();
        Assert.assertFalse(decorator.isCalled());
    }
    
    @Test
    public void StatementsUndefined_NotCalled() {
        resourceMock.givenScope(Scopes.FILE);
        givenNcLoc(10.0);
        createDecorator();
        Assert.assertFalse(decorator.isCalled());
    }

    private void givenNcLoc(double value) {
        decoratorContextMock.givenMeasure(CoreMetrics.NCLOC,value);
    }
    
    private void givenStatements(double value) {
        decoratorContextMock.givenMeasure(CoreMetrics.STATEMENTS,value);         
    }
    
    class PrimitiveDecorator extends BaseDecorator {

        private boolean called=false;
        
        protected PrimitiveDecorator(MsCoverProperties msCoverProperties, TimeMachine timeMachine) {
            super(msCoverProperties, timeMachine);
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

        @Override
        public boolean shouldExecuteDecorator(Project project,
                MsCoverProperties propertiesHelper) {
            return false;
        }
        
    }
}
