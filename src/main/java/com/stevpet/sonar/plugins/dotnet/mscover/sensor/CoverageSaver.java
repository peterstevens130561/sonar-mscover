package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.resources.Project;

public interface CoverageSaver {

    /**
     * Save the line and block measures
     * @param project current project
     * @param path to the coverage.xml file
     */

    void analyse(Project project, String path);

}