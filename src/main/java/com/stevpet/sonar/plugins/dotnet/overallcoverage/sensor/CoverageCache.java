package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface CoverageCache {

    /**
     * query coverage of the module
     * @param string
     * @return coverage, or null
     */
    SonarCoverage get(String string);

    /**
     * merge coverage into the cache.
     * @param coverage
     * @param string
     */
    void merge(SonarCoverage coverage, String string);

}
