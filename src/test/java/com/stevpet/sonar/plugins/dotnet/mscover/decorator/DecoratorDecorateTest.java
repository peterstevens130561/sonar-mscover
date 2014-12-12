package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Scopes;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.ResourceMock;

import static org.mockito.Mockito.mock;

public class DecoratorDecorateTest {
    private ResourceMock resourceMock = new ResourceMock();

    private Settings settings;
    private DecoratorContextMock decoratorContextMock = new DecoratorContextMock();
    private CoverageDecoratorStub decorator;
    @Before
    public void before() {
        settings = mock(Settings.class);
        decorator = new CoverageDecoratorStub(settings);
    }
    
    @Test
    public void Decorate_NotFile_NothingDone() {
        //Given
        resourceMock.givenScope(Scopes.DIRECTORY);
        decoratorContextMock.givenMeasure(CoreMetrics.NCLOC, null);   
        //When
        decorator.decorate(resourceMock.getMock(),null);
        //Then
        Assert.assertEquals(0, decorator.getCalls());
    }
    
    public void Decorate_EmptyFile_NothingDone() {
        //Given
        resourceMock.givenScope(Scopes.FILE);
        decoratorContextMock.givenMeasure(CoreMetrics.NCLOC, null);   
        //When
        decorator.decorate(resourceMock.getMock(),decoratorContextMock.getMock());
        //Then
        Assert.assertEquals(0, decorator.getCalls());
    }
}

