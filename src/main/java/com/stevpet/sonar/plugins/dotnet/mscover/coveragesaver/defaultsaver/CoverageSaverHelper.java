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
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

public interface CoverageSaverHelper {

    /**
     * 
     * @param coveragePoints the coverage info
     * @param metric - the LINE_HITS metric to include in the measure
     * @return created measure which has for each line in the coveragePoints whether it is covered or not. Data is ordered by line. Lines not included are skipped
     */
     abstract Measure<?> getCoveredHitData(CoverageLinePoints coveragePoints, Metric<?> metric);

    /**
     * 
     * @param coveragePoints the coverage info
     * @param metric - the LINE_HITS metric to include in the measure
     * @return created measure which has for each line in the coveragePoints whether it should be covered or not.
     */
    abstract Measure<?> getToCoverHitData(CoverageLinePoints coveragePoints, Metric<?> metric);
    
    double convertPercentage(Number percentage) ;

}