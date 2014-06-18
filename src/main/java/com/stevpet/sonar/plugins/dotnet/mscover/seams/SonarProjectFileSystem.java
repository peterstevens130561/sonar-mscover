package com.stevpet.sonar.plugins.dotnet.mscover.seams;

import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;

@SuppressWarnings("deprecation")
public class SonarProjectFileSystem {
    
    ProjectFileSystem projectFileSystem ;
    public SonarProjectFileSystem(Project project) {
        projectFileSystem = project.getFileSystem();
    }
}
