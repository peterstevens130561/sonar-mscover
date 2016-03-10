package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

public class UnitTestBranchCoverageMetrics implements BranchCoverageMetrics {

    @Override
    public Metric getUncoveredMetric() {
        return CoreMetrics.UNCOVERED_CONDITIONS;
    }
    
    @Override
    public Metric getToCoverMetric() {
        return CoreMetrics.CONDITIONS_TO_COVER;
    }
    
    @Override
    public Metric getCoverageMetric() {
        return CoreMetrics.BRANCH_COVERAGE;
    }
    
    @Override
    public Metric getToCoverByLineMetric() {
        return CoreMetrics.CONDITIONS_BY_LINE;
    }
    
    @Override
    public Metric getCoveredByLineMetric() {
        return CoreMetrics.COVERED_CONDITIONS_BY_LINE;
    }

}

