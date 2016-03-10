package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

public class IntegrationTestBranchCoverageMetrics implements BranchCoverageMetrics {

    @Override
    public Metric getUncoveredMetric() {
        return CoreMetrics.IT_UNCOVERED_CONDITIONS;
    }
    
    @Override
    public Metric getToCoverMetric() {
        return CoreMetrics.IT_CONDITIONS_TO_COVER;
    }
    
    @Override
    public Metric getCoverageMetric() {
        return CoreMetrics.IT_BRANCH_COVERAGE;
    }
    
    @Override
    public Metric getToCoverByLineMetric() {
        return CoreMetrics.IT_CONDITIONS_BY_LINE;
    }
    
    @Override
    public Metric getCoveredByLineMetric() {
        return CoreMetrics.IT_COVERED_CONDITIONS_BY_LINE;
    }

}
