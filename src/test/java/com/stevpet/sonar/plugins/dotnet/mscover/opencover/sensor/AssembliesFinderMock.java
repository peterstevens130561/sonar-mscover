package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import java.io.File;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;

public class AssembliesFinderMock extends GenericClassMock<AssembliesFinder> {

    public AssembliesFinderMock() {
        super(AssembliesFinder.class);
    }

    public void onFindUnitTestAssembliesDir(String assembliesPath) {
        when( instance.findUnitTestAssembliesDir(any(VisualStudioSolution.class))).thenReturn(assembliesPath);
    }
}
