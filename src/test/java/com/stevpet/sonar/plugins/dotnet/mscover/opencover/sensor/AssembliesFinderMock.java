package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;

public class AssembliesFinderMock extends GenericClassMock<AssembliesFinder> {

    public AssembliesFinderMock() {
        super(AssembliesFinder.class);
    }

    public void onFindUnitTestAssembliesDir(String assembliesPath) {
        when( instance.findUnitTestAssembliesDir(any(File.class))).thenReturn(assembliesPath);
    }
}
