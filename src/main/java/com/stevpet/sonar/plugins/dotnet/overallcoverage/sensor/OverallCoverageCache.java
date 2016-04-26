package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;


import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

/**
 * maintains the overall coverage data (merged result of integration tests and unit test)
 * @author stevpet
 *
 */
public interface OverallCoverageCache {

    /**
     * query coverage of the module
     * @param moduleName
     * @return coverage, or null
     */
    SonarCoverage get(String moduleName);

    /**
     * merge coverage into the overall cache.
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
