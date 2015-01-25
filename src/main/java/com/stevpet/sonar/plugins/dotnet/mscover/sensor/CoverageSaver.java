package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

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

}