package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.resources.Project;

public interface ShouldExecuteHelper {

    public abstract boolean shouldExecuteOnProject(Project project);

}