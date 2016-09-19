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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

@InstantiationStrategy(value=InstantiationStrategy.PER_BATCH)
public class DefaultOverallCoverageCache implements OverallCoverageRepository,BatchExtension {
    private final Logger LOG = LoggerFactory.getLogger(DefaultOverallCoverageCache.class);
    private Map<String, SonarCoverage> map = new HashMap<>();

    @Override
    public SonarCoverage get(String moduleName) {
        LOG.debug("get {}",moduleName);
        return map.get(moduleName);
    }

    @Override
    public void merge(SonarCoverage coverage, String moduleName) {
        LOG.debug("merging {}",moduleName);
        if (!map.containsKey(moduleName)){
            map.put(moduleName, coverage);
        } else {
            SonarCoverage dest = map.get(moduleName);
            dest.merge(coverage);

        }
    }

    @Override
    public void delete(String moduleName) {
        map.remove(moduleName);
    }
    
    

}