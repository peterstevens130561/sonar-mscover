package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;

public interface CoverageFileLocator {


    /**
     * When running tests for a project, then the coverage file will be split per assembly. Each file must have a unique name. 
     * In the saving phase we want to find them per project, and the project names are equal to the assemblyName, so the created path
     * is the concatenation of root, assemblyName and project, the latter just to make it unique.
     * @param root 
     * @param projectName - the project currently being analysed
     * @param assemblyName - the covered assembly (module in the coverage file)
     * @return concatenation of root, assembly, project
     */
    File getFile(File root, String projectName, String assemblytName);

    /**
     * 
     * get the directory where all coverage files of a project are stored
     * @param root
     * @param projectName
     * @return
     */
    File getProjectDir(File root, String assemblytName);
}