package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.Metric;

public interface BranchCoverageMetrics {
    /**
     * array of strings showing paths covered at line level
     * @return
     */
    Metric<String> getCoveredByLineMetric();
    /**
     * array of strings showing number of paths to cover by line
     * @return
     */
    Metric<String> getToCoverByLineMetric();
    
    /**
     * percentage
     * @return
     */
    Metric<Double> getCoverageMetric() ;
    
    /**
     * number of branches to cover
     * @return
     */
    Metric<Integer> getToCoverMetric() ;
    /**
     * number of branches covered
     * @return
     */
    Metric<Integer> getUncoveredMetric() ;
}
