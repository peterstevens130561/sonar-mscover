package com.stevpet.sonar.plugins.dotnet.mscover.mock;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.VisualStudioProjectMock;
import static org.mockito.Mockito.when;

public class VisualStudioSolutionMock extends GenericClassMock<VisualStudioSolution> {

    List<VisualStudioProject> projects;
    public VisualStudioSolutionMock() {
        super(VisualStudioSolution.class);
    }

    public void givenProject(VisualStudioProject visualStudioProject) {
        if(projects==null) {
            projects = new ArrayList<VisualStudioProject>();
        }
        projects.add(visualStudioProject);
        when(instance.getProjects()).thenReturn(projects);
    }
    
    public void givenProjectMock(VisualStudioProjectMock visualStudioProjectMock) {
        givenProject(visualStudioProjectMock.getMock());
    }

}
