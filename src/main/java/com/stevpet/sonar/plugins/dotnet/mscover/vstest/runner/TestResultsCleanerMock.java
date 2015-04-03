package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import static org.mockito.Mockito.verify;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class TestResultsCleanerMock extends GenericClassMock<TestResultsCleaner> {

    public TestResultsCleanerMock() {
        super(TestResultsCleaner.class);
    }
    
    public void thenExecuteInvoked() {
        verify(instance).execute();
    }
}
