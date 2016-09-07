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
