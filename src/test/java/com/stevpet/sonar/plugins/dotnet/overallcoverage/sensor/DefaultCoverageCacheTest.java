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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class DefaultCoverageCacheTest {

    private static final String MODULE_NAME = "bla";
    OverallCoverageRepository coverageCache ;
    private DefaultProjectCoverageRepository coverage;
    @Before()
    public void before() {
        coverageCache = new DefaultOverallCoverageCache();
    }
    
    @Test
    public void noCoverage() {
        coverage=coverageCache.get(MODULE_NAME);
        assertNull("should not have data",coverage);
    }
    
    @Test
    public void insertFirst() {
        coverage = new DefaultProjectCoverageRepository();

        coverage.linkFileNameToFileId("a", "1");
        coverageCache.merge(coverage,MODULE_NAME);
        assertNotNull("should be valid object",coverage);
        assertEquals("should have one file",1,coverage.getValues().size());
    }
    
    @Test
    public void insertSecond() {
        coverage = new DefaultProjectCoverageRepository();
        coverage.linkFileNameToFileId("a", "1");
        coverageCache.merge(coverage,MODULE_NAME);
        coverage = new DefaultProjectCoverageRepository();
        coverage.linkFileNameToFileId("b", "2");
        coverageCache.merge(coverage, MODULE_NAME);
        ProjectCoverageRepository coverageGotten = coverageCache.get(MODULE_NAME);
        assertNotNull("should be valid object",coverageGotten);
        assertEquals("should have one file",2,coverageGotten.getValues().size());
    }
    
    @Test
    public void insertThenRemove() {
        coverage = new DefaultProjectCoverageRepository();

        coverage.linkFileNameToFileId("a", "1");
        coverageCache.merge(coverage,MODULE_NAME);
        coverageCache.delete(MODULE_NAME);
        ProjectCoverageRepository coverageGotten = coverageCache.get(MODULE_NAME);
        assertNull("should not have data, as it is deleted",coverageGotten);
    }
}
