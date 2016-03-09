package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface CoverageCache {

    /**
     * query coverage of the module
     * @param module
     * @return coverage, or null
     */
    SonarCoverage get(Project module);

}
