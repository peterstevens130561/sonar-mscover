package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper;

import java.io.File;
import java.util.List;

import org.sonar.api.batch.InstantiationStrategy;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public interface MicrosoftWindowsEnvironment {

    VisualStudioSolution getCurrentSolution();


    void setCurrentSolution(VisualStudioSolution currentSolution);
    
    List<File> getUnitTestSourceFiles();
    
    public List<String> getAssemblies();

}