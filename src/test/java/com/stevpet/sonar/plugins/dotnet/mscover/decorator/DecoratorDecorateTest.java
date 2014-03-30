package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DecoratorDecorateTest {

    private Resource resource ;
    private Settings settings;
    private DecoratorContext context;
    private CoverageDecoratorStub decorator;
    @Before
    public void before() {
        resource = mock(Resource.class);
        settings = mock(Settings.class);
        context = mock(DecoratorContext.class);
        decorator = new CoverageDecoratorStub(settings);
    }
    
    @Test
    public void Decorate_NotFile_NothingDone() {
        //Arrange
        when(resource.getScope()).thenReturn(Scopes.DIRECTORY);
        when(context.getMeasure(CoreMetrics.NCLOC)).thenReturn(null);
        
        //Act
        decorator.decorate(resource,null);
        //Assert
        Assert.assertEquals(0, decorator.getCalls());
    }
    
    public void Decorate_EmptyFile_NothingDone() {
        //Arrange
        when(resource.getScope()).thenReturn(Scopes.FILE);
        when(context.getMeasure(CoreMetrics.NCLOC)).thenReturn(null);
        
        //Act
        decorator.decorate(resource,context);
        //Assert
        Assert.assertEquals(0, decorator.getCalls());
    }
}

