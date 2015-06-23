package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class AssemblyLocatorMock extends GenericClassMock<AssemblyLocator> {

    public AssemblyLocatorMock() {
        super(AssemblyLocator.class);
    }

    public void givenLocate(String projectName, File file) {
        when(instance.locateAssembly(eq(projectName), any(File.class),any(VisualStudioProject.class))).thenReturn(file);
    }

}
