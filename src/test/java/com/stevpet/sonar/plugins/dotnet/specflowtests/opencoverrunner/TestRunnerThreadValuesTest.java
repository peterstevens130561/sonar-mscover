package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TestRunnerThreadValuesTest {
    private static final String PROJECT_NAME = "ProjectName";
    private TestRunnerThreadValues threadValues;
    @Mock private Future<Boolean> future;
    @Mock private Callable<Boolean> callable;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        threadValues = new TestRunnerThreadValues(future, callable, PROJECT_NAME);
    }
    
    @Test
    public void isSameFuture() {
        assertEquals("future gotten is same as inserted",future,threadValues.getFuture());
    }
    
    @Test
    public void isSameCallable() {
        assertEquals("callable gotten is samee as inserted",callable,threadValues.getCallable());
    }
    
    @Test
    public void isSameName() {
        assertEquals("project name gotten is same as inserted",PROJECT_NAME,threadValues.getName());
    }
}
