package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.Metric;

public interface BranchCoverageMetrics {
    Metric<?> getCoveredByLineMetric();
    Metric<?> getToCoverByLineMetric();
    Metric<?> getCoverageMetric() ;
    Metric<?> getToCoverMetric() ;
    Metric<?> getUncoveredMetric() ;
}
