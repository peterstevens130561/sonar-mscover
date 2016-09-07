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
package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;


import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

/**
 * maintains the overall coverage data (merged result of integration tests and unit test)
 * @author stevpet
 *
 */
public interface OverallCoverageCache {

    /**
     * query coverage of the module
     * @param moduleName
     * @return coverage, or null
     */
    SonarCoverage get(String moduleName);

    /**
     * merge coverage into the overall cache.
     * @param coverage
     * @param moduleName
     */
    void merge(SonarCoverage coverage, String moduleName);

    /**
     * remove coverage from the cache
     * @param moduleName
     */
    void delete(String moduleName);

}
