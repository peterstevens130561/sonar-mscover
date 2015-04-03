package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class VsTestConfigFinderMock extends GenericClassMock<VsTestConfigFinder> {

    public VsTestConfigFinderMock() {
        super(VsTestConfigFinder.class);
    }

    public void givenGetTestSettingsFileOrDie(File file) {
        when(instance.getTestSettingsFileOrDie(any(File.class), anyString())).thenReturn(file);
    }


}
