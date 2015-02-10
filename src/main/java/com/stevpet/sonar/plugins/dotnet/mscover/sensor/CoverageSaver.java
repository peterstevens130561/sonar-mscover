package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import java.io.File;
import java.util.List;

import org.sonar.api.resources.Project;

public interface CoverageSaver {

    /**
     * Save the line and block measures
     * @param project current project
     * @param path to the coverage.xml file
     * @param modules to parse
     */

    void analyse(Project project, String path,List<String>modules);

    /**
     * Save the line measures of a set of vsTest coverage xml files
     * @param project 
     * @param xmlFiles files to include
     * @param artifactNames in this project
     */
    void analyse(Project project, List<File> xmlFiles,
            List<String> artifactNames);

}