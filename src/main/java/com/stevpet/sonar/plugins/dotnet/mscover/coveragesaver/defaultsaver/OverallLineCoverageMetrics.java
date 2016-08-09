package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

public class OverallLineCoverageMetrics implements LineCoverageMetrics {

    @Override
    public Metric<?> getLinesToCoverMetric() {
        return CoreMetrics.OVERALL_LINES_TO_COVER;
    }

    @Override
    public Metric<?> getUncoveredLinesMetric() {
        return CoreMetrics.OVERALL_UNCOVERED_LINES;
    }

    @Override
    public Metric<?> getCoverageMetric() {
        return CoreMetrics.OVERALL_COVERAGE;
    }

    @Override
    public Metric<Double> getLineCoverageMetric() {
        return CoreMetrics.OVERALL_LINE_COVERAGE;
    }

    @Override
    public Metric<String> getCoverageLineHitsDataMetric() {
        return CoreMetrics.OVERALL_COVERAGE_LINE_HITS_DATA;
    }

}
