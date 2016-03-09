package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;


import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface OverallCoverageCache {

    /**
     * query coverage of the module
     * @param moduleName
     * @return coverage, or null
     */
    SonarCoverage get(String moduleName);

    /**
     * merge coverage into the cache.
     * @param coverage
     * @param moduleName
     */
    void merge(SonarCoverage coverage, String moduleName);

    /**
     * remove coverage from the cache
     * @param moduleName
     */
    void delete(String moduleName);

}
