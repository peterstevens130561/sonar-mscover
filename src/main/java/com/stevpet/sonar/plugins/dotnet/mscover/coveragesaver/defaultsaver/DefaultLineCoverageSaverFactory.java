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

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultLineCoverageSaverFactory implements LineCoverageSaverFactory {
    private final ResourceResolver resourceResolver;
    private final CoverageSaverHelper coverageSaverHelper;
    
    public DefaultLineCoverageSaverFactory(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.coverageSaverHelper = new DefaultCoverageSaverHelper();
    }
    @Override
    public LineFileCoverageSaver createIntegrationTestSaver() {
        return new DefaultLineFileCoverageSaver(new IntegrationTestLineCoverageMetrics(), resourceResolver, coverageSaverHelper);
    }

    @Override
    public LineFileCoverageSaver createUnitTestSaver() {
        return new DefaultLineFileCoverageSaver(new UnitTestLineCoverageMetrics(), resourceResolver, coverageSaverHelper);
    }

    @Override
    public LineFileCoverageSaver createOverallSaver() {
        return new DefaultLineFileCoverageSaver(new OverallLineCoverageMetrics(), resourceResolver, coverageSaverHelper);
    }

}
