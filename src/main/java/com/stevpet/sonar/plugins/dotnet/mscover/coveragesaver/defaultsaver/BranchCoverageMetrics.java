package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.Metric;

public interface BranchCoverageMetrics {
    Metric<String> getCoveredByLineMetric();
    Metric<String> getToCoverByLineMetric();
    Metric<Double> getCoverageMetric() ;
    Metric<Integer> getToCoverMetric() ;
    Metric<Integer> getUncoveredMetric() ;
}
