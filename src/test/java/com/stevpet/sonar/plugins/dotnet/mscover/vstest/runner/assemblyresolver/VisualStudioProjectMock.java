package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import java.io.File;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.when;

public class VisualStudioProjectMock extends GenericClassMock<VisualStudioProject> {
 
    public VisualStudioProjectMock() {
        super(VisualStudioProject.class);
    }

    public void givenDirectory(File directory) {
       when(instance.getDirectory()).thenReturn(directory);
    }

    public void givenArtifactName(String artifactName) {
        when(instance.getArtifactName()).thenReturn(artifactName);
    }
}
