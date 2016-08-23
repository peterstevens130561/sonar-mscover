package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

public interface CoverageSaverHelper {

    /**
     * 
     * @param coveragePoints the coverage info
     * @param metric - the LINE_HITS metric to include in the measure
     * @return created measure which has for each line in the coveragePoints whether it is covered or not. Data is ordered by line. Lines not included are skipped
     */
     abstract Measure<?> getCoveredHitData(CoverageLinePoints coveragePoints, Metric<?> metric);

    /**
     * 
     * @param coveragePoints the coverage info
     * @param metric - the LINE_HITS metric to include in the measure
     * @return created measure which has for each line in the coveragePoints whether it should be covered or not.
     */
    abstract Measure<?> getToCoverHitData(CoverageLinePoints coveragePoints, Metric<?> metric);
    
    double convertPercentage(Number percentage) ;

}