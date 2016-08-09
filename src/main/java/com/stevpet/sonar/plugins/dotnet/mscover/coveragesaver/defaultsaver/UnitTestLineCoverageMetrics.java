package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

public class UnitTestLineCoverageMetrics implements LineCoverageMetrics {

    @Override
    public Metric<?> getLinesToCoverMetric() {
        return CoreMetrics.LINES_TO_COVER;
    }

    @Override
    public Metric<?> getUncoveredLinesMetric() {
        return CoreMetrics.UNCOVERED_LINES;
    }

    @Override
    public Metric<?> getCoverageMetric() {
        return CoreMetrics.COVERAGE;
    }

    @Override
    public Metric<Double> getLineCoverageMetric() {
        return CoreMetrics.LINE_COVERAGE;
    }

    @Override
    public Metric<String> getCoverageLineHitsDataMetric() {
        return CoreMetrics.COVERAGE_LINE_HITS_DATA;
    }

}
