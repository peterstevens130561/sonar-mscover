package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.ArrayList;
import java.util.List;

import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class MicrosoftWindowsEnvironmentMock {
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
    private VisualStudioSolution solution = mock(VisualStudioSolution.class);


    public void givenHasNoTestProjects() {
        when(microsoftWindowsEnvironment.getCurrentSolution()).thenReturn(solution);
        List<VisualStudioProject> value = new ArrayList<VisualStudioProject>();
        when(solution.getUnitTestProjects()).thenReturn(value);
    }
    
    public MicrosoftWindowsEnvironment getMock() {
        return microsoftWindowsEnvironment;
    }

    public void givenTestsHaveExecuted(boolean flag) {
        when(microsoftWindowsEnvironment.isTestExecutionDone()).thenReturn(flag);
    }

    public void givenHasTestProject() {
        when(microsoftWindowsEnvironment.getCurrentSolution()).thenReturn(solution);
        List<VisualStudioProject> unitTestProjects = new ArrayList<VisualStudioProject>();
        VisualStudioProject vsProject = new VisualStudioProject();
        unitTestProjects.add(vsProject);
        when(solution.getUnitTestProjects()).thenReturn(unitTestProjects);       
    }
    
    public void givenHasSolution(VisualStudioSolution solution) {
        when(microsoftWindowsEnvironment.getCurrentSolution()).thenReturn(solution);
    }

    public void givenHasSolutionWithProject(int count) {
        when(microsoftWindowsEnvironment.getCurrentSolution()).thenReturn(solution);
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        for(int projectIndex=0;projectIndex<count;projectIndex++) {
            VisualStudioProject vsProject = mock(VisualStudioProject.class);
            when(vsProject.getAssemblyName()).thenReturn("project" + projectIndex + ".dll");
            projects.add(vsProject);
        }
        when(solution.getUnitTestProjects()).thenReturn(projects);               // TODO Auto-generated method stub
        
    }
    
}
