package com.stevpet.sonar.plugins.dotnet.mscover.seams;

import java.io.File;

import org.sonar.api.resources.Project;

public interface ProjectSeam {
    boolean isRoot();

    void setProject(Project project);
    File getSonarWorkingDirectory();

    File getSonarFile(String name);
}
