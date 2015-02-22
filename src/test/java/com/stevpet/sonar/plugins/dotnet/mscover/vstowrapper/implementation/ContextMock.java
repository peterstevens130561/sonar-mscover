package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;

import org.sonar.api.batch.bootstrap.ProjectBuilder.Context;
import org.sonar.api.batch.bootstrap.ProjectReactor;
import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class ContextMock extends GenericClassMock<Context> {

    public ContextMock() {
        super(Context.class);
    }

    public void givenProjectReactor(ProjectReactor projectReactor) {
            when(instance.projectReactor()).thenReturn(projectReactor);
        
    }

}
