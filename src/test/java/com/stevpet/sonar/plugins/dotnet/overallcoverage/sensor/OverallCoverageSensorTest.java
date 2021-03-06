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
package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;

public class OverallCoverageSensorTest {

    private static final String MODULE_NAME = "bla";
    private Sensor sensor;
    @Mock private Project module;
    @Mock private SensorContext context;
    @Mock private OverallCoverageRepository coverageCache;
    @Mock private CoverageSaver coverageSaver;

    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        sensor = new OverallCoverageSensor(coverageCache,coverageSaver);
        when(module.getName()).thenReturn(MODULE_NAME);
    }
    
    @Test
    public void testAnalyse() {
        DefaultProjectCoverageRepository sonarCoverage = new DefaultProjectCoverageRepository();
        when(coverageCache.get(MODULE_NAME)).thenReturn(sonarCoverage);
        sensor.analyse(module, context);
        verify(coverageCache,times(1)).get(MODULE_NAME);
        verify(coverageSaver,times(1)).save(context, sonarCoverage);
    }
    
    @Test
    public void testNoCoverage() {
        when(coverageCache.get(MODULE_NAME)).thenReturn(null);
        sensor.analyse(module, context);
        verify(coverageCache,times(1)).get(MODULE_NAME);
        verify(coverageSaver,times(0)).save(context, null);
    }

}
