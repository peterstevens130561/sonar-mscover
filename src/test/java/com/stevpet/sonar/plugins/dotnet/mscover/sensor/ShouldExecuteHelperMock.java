package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.when;
public class ShouldExecuteHelperMock extends GenericClassMock<ShouldExecuteHelper> {

    public ShouldExecuteHelperMock() {
        super(ShouldExecuteHelper.class);
    }

    /**
     * when shouldExecuteOnProject is invoked for thisproject, then the result is returned.
     * @param project 
     * @param result
     */
    public void whenShouldExecute(Project project, boolean result) {
        when(instance.shouldExecuteOnProject(project)).thenReturn(result);
    }

}
