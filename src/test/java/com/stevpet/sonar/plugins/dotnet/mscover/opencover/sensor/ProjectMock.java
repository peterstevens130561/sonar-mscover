package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.mockito.Mockito.when;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;

public class ProjectMock extends GenericClassMock<Project>{
    public ProjectMock() {
        super(Project.class);
    }
    

    public void givenIsRootProject(boolean isRoot) {
        when(instance.isRoot()).thenReturn(isRoot);       
    }
    public void givenIsCSharpProject(boolean b) {
        if(b) {
        when(instance.getLanguageKey()).thenReturn("cs");
        } else {
            when(instance.getLanguageKey()).thenReturn("blurp");          
        }
    }

    public void givenFileSystem(DummyFileSystem dummyFileSystem) {
        when(instance.getFileSystem()).thenReturn( new DummyFileSystem()); 
    }


    /**
     * 
     * @param root - to return when isRoot is invoked
     */
    public void givenIsRoot(boolean root) {
            when(instance.isRoot()).thenReturn(root);
    }

    /**
     * 
     * @param name - to return when getName is invoked
     */
    public void givenName(String name) {
        when(instance.getName()).thenReturn(name);
    }
}
