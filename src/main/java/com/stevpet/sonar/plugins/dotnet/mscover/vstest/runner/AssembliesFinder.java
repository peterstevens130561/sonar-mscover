package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;

public interface AssembliesFinder {

    List<String> findUnitTestAssembliesFromConfig(File solutionDirectory,
            List<VisualStudioProject> projects);
    
    
    File searchNonExistingFile(File assemblyFile, VisualStudioProject project, String buildConfiguration);

    String findUnitTestAssembliesDir(VisualStudioSolution solution);
    

}