/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

/**
 * to be used by the CoverageSavers
 *
 */
public class DefaultCoverageSaverHelper implements CoverageSaverHelper{

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverHelper#getCoveredHitData(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints, org.sonar.api.measures.Metric)
     */
    @Override
    public Measure<?> getCoveredHitData(CoverageLinePoints coveragePoints, Metric<?> metric) {
        return getLineHitData(coveragePoints, metric, point -> point.getCovered());
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverHelper#getToCoverHitData(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints, org.sonar.api.measures.Metric)
     */
    @Override
    public Measure<?> getToCoverHitData(CoverageLinePoints coveragePoints, Metric<?> metric) {
        return getLineHitData(coveragePoints, metric, point -> point.getToCover());
    }

    private interface Strategy {
        int data(CoverageLinePoint point);
    }
    
    private Measure<?> getLineHitData(CoverageLinePoints coveragePoints, Metric<?> metric, Strategy strategy) {
        PropertiesBuilder<Integer, Integer> propertiesBuilder = new PropertiesBuilder<Integer, Integer>(metric);
        coveragePoints.getPoints()
        .forEach(point -> propertiesBuilder
                .add(
                        point.getLine(), 
                        strategy.data(point)
                )
        );
        return propertiesBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
    }

    @Override
    public double convertPercentage(Number percentage) {
        return  ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }

}
