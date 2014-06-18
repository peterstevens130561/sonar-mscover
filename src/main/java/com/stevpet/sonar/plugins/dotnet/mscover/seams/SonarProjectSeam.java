package com.stevpet.sonar.plugins.dotnet.mscover.seams;

import org.sonar.api.resources.Project;

public class SonarProjectSeam implements ProjectSeam {

    private final Project project ;

    private SonarProjectSeam(Project project) {
        this.project = project ;
    }
    
    public static SonarProjectSeam create(Project project) {
        return new SonarProjectSeam(project) ;
    }

    public boolean isRoot() {
        return project.isRoot();
    }
}
