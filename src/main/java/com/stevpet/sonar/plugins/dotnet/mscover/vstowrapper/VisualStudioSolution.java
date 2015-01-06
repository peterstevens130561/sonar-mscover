package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper;

import java.io.File;
import java.util.List;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation.VisualStudioSolutionProject;

public interface VisualStudioSolution {

    File getSolutionDir();

    List<VisualStudioProject> getProjects();

    List<VisualStudioProject> getUnitTestProjects();

    VisualStudioProject getProjectFromSonarProject(Project project);

    VisualStudioProject getProject(File file);

    List<VisualStudioSolutionProject> projects();

}