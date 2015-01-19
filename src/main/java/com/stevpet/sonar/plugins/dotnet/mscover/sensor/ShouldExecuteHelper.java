package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.resources.Project;

public interface ShouldExecuteHelper {

    boolean shouldExecuteOnProject(Project project);

}