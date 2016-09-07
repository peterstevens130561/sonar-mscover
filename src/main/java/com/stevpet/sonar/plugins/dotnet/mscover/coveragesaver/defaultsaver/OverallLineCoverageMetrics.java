/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
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
