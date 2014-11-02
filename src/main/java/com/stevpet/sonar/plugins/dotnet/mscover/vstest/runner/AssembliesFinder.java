package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.util.List;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

public interface AssembliesFinder {

    List<String> findUnitTestAssembliesFromConfig(File solutionDirectory,
            List<VisualStudioProject> projects);
    
    File searchNonExistingFile(File assemblyFile, VisualStudioProject project, String buildConfiguration);
    

}