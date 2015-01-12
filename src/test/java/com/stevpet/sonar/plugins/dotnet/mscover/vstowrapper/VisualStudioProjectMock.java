package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.when;
public class VisualStudioProjectMock extends GenericClassMock<VisualStudioProject> {
    public VisualStudioProjectMock() {
        super(VisualStudioProject.class);
    }

    public void givenOutputType(String outputType) {
        when(instance.outputType()).thenReturn(outputType);
    }

    public void givenAssemblyName(String assemblyName) {
        when(instance.getAssemblyName()).thenReturn(assemblyName);
    }
}
