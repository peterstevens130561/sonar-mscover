package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.sonar.api.resources.Project;

public class ProjectMock {
    private Project project = mock(Project.class);
    
    public Project getMock() {
        return project ;
    }

    public void givenIsRootProject(boolean isRoot) {
        when(project.isRoot()).thenReturn(isRoot);       
    }
    public void givenIsCSharpProject(boolean b) {
        if(b) {
        when(project.getLanguageKey()).thenReturn("cs");
        } else {
            when(project.getLanguageKey()).thenReturn("blurp");          
        }
    }
}
