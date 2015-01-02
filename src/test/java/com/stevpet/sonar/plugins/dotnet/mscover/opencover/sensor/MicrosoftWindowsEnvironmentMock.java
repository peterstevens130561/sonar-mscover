package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper..VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class MicrosoftWindowsEnvironmentMock extends GenericClassMock<MicrosoftWindowsEnvironment>{
    private VisualStudioSolution solution = mock(VisualStudioSolution.class);

    public MicrosoftWindowsEnvironmentMock() {
        super(MicrosoftWindowsEnvironment.class);
        when(instance.getWorkingDirectory()).thenReturn(".sonar");
    }

    public void givenHasNoTestProjects() {
        when(instance.getCurrentSolution()).thenReturn(solution);
        List<VisualStudioProject> value = new ArrayList<VisualStudioProject>();
        when(solution.getUnitTestProjects()).thenReturn(value);
    }
    

    public void givenTestsHaveExecuted(boolean flag) {
        when(instance.isTestExecutionDone()).thenReturn(flag);
    }

    public void givenHasTestProject() {
        when(instance.getCurrentSolution()).thenReturn(solution);
        List<VisualStudioProject> unitTestProjects = new ArrayList<VisualStudioProject>();
        VisualStudioProject vsProject = new VisualStudioProject();
        unitTestProjects.add(vsProject);
        when(solution.getUnitTestProjects()).thenReturn(unitTestProjects);       
    }
    
    public void givenHasSolution(VisualStudioSolution solution) {
        when(instance.getCurrentSolution()).thenReturn(solution);
    }

    public void givenHasSolutionWithProject(int count) {
        when(instance.getCurrentSolution()).thenReturn(solution);
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        for(int projectIndex=0;projectIndex<count;projectIndex++) {
            VisualStudioProject vsProject = mock(VisualStudioProject.class);
            when(vsProject.getAssemblyName()).thenReturn("project" + projectIndex + ".dll");
            projects.add(vsProject);
        }
        when(solution.getUnitTestProjects()).thenReturn(projects);               // TODO Auto-generated method stub
        
    }
    
}
