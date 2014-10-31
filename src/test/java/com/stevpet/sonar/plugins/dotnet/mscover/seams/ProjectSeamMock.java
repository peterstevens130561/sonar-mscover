package com.stevpet.sonar.plugins.dotnet.mscover.seams;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.when;

public class ProjectSeamMock extends GenericClassMock<ProjectSeam> {

    public ProjectSeamMock() {
        super(ProjectSeam.class);
    }

    public ProjectSeamMock givenSonarWorkingDirectory(File workingDir) {
        when(instance.getSonarWorkingDirectory()).thenReturn(workingDir);
        return this;
    }

    
}
