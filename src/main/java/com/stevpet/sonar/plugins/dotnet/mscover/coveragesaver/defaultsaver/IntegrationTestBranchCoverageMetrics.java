package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

public class IntegrationTestBranchCoverageMetrics implements BranchCoverageMetrics {

    @Override
    public Metric<Integer> getUncoveredMetric() {
        return CoreMetrics.IT_UNCOVERED_CONDITIONS;
    }
    
    @Override
    public Metric<Integer> getToCoverMetric() {
        return CoreMetrics.IT_CONDITIONS_TO_COVER;
    }
    
    @Override
    public Metric<Double> getCoverageMetric() {
        return CoreMetrics.IT_BRANCH_COVERAGE;
    }
    
    @Override
    public Metric<String> getToCoverByLineMetric() {
        return CoreMetrics.IT_CONDITIONS_BY_LINE;
    }
    
    @Override
    public Metric<String> getCoveredByLineMetric() {
        return CoreMetrics.IT_COVERED_CONDITIONS_BY_LINE;
    }

}
