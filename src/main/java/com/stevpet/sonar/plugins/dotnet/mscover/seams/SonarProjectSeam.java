package com.stevpet.sonar.plugins.dotnet.mscover.seams;

import java.io.File;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class SonarProjectSeam   implements ProjectSeam{

    private Project project ;

    public SonarProjectSeam(Project project) {
        this.project = project ;
    }
    
    public SonarProjectSeam() {

    }

    public void setProject(Project project) {
        this.project=project;
    }
    public static SonarProjectSeam create(Project project) {
        return new SonarProjectSeam(project) ;
    }

    public boolean isRoot() {
        return project.isRoot();
    }

    @SuppressWarnings("deprecation")
    public File getSonarWorkingDirectory() {
        return project.getFileSystem().getSonarWorkingDirectory();
    }

    public File getSonarFile(String name) {
        File sonarWorkingDirectory=getSonarWorkingDirectory();
        return new File(sonarWorkingDirectory,name);
    }
}
