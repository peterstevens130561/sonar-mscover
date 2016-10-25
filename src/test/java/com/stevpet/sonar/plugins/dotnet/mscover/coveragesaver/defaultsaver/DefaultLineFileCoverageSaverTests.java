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

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.SensorContext;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultLineFileCoverageSaverTests {
    private LineCoverageMetrics lineCoverageMetrics;
    @Mock private ResourceResolver resourceResolver;
    @Mock private CoverageSaverHelper coverageSaverHelper;
    @Mock private CoverageLinePoints coveragePoints;
    @Mock private File file;
    @Mock private SensorContext sensorContext;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        lineCoverageMetrics = new OverallLineCoverageMetrics(); // makes no sense to mock
        new DefaultLineFileCoverageSaver(lineCoverageMetrics, resourceResolver, coverageSaverHelper);
    }
    
    // @Test
    
    //TODO: make a useful test
    public void normal() {
        //lineFileCoverageSaver.saveMeasures(sensorContext,file, coveragePoints);
    }
    
}
