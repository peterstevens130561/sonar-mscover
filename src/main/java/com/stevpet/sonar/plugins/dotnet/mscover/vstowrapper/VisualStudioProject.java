package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper;

import java.io.File;
import java.util.List;

public interface VisualStudioProject {

    /**
     * get the name of the assembly generated by this project, without the extension
     * @return
     */
    String getAssemblyName();

    /**
     * Get the file created by this project for the given arguments
     * @param buildConfiguration i.e. Debug, Release
     * @param buildPlatform i.e AnyCPU, x64
     * @return
     */
    File getArtifact(String buildConfiguration, String buildPlatform);

    boolean isUnitTest();

    String getArtifactName();

    File getDirectory();

    String getName();

    boolean isTest();

    String outputType();
    
    List<File> getSourceFiles();

}