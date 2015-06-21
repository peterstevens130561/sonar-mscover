/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
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
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;


public class UnitTestBlockDecorator extends BaseDecorator {
    public UnitTestBlockDecorator(MsCoverProperties propertiesHelper, TimeMachine timeMachine) {
        super(propertiesHelper, timeMachine);
        this.executionMode="active";
        this.testMetric = CoreMetrics.BRANCH_COVERAGE;
    }

    @Override
    public boolean shouldExecuteDecorator(Project project, MsCoverProperties propertiesHelper) {
        return propertiesHelper.isUnitTestsEnabled();
    }

    @Override
    protected void handleUncoveredResource(DecoratorContext context, double blocks) {
      context.saveMeasure(CoreMetrics.BRANCH_COVERAGE, 0.0);
      context.saveMeasure(CoreMetrics.UNCOVERED_CONDITIONS, blocks);
      context.saveMeasure(CoreMetrics.CONDITIONS_TO_COVER, blocks);
    }
    
    @DependedUpon
    public List<Metric> generatesCoverageMetrics() {
      return Arrays.asList(CoreMetrics.BRANCH_COVERAGE,CoreMetrics.UNCOVERED_CONDITIONS,CoreMetrics.CONDITIONS_TO_COVER, CoreMetrics.COVERED_CONDITIONS_BY_LINE,CoreMetrics.COVERED_CONDITIONS_BY_LINE);
    }

}
