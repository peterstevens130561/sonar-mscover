package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class DefaultCoverageCacheTest {

    private static final String MODULE_NAME = "bla";
    CoverageCache coverageCache ;
    private SonarCoverage coverage;
    @Before()
    public void before() {
        coverageCache = new DefaultCoverageCache();
    }
    
    @Test
    public void noCoverage() {
        coverage=coverageCache.get(MODULE_NAME);
        assertNull("should not have data",coverage);
    }
    
    @Test
    public void insertFirst() {
        coverage = new SonarCoverage();

        coverage.linkFileNameToFileId("a", "1");
        coverageCache.merge(coverage,MODULE_NAME);
        assertNotNull("should be valid object",coverage);
        assertEquals("should have one file",1,coverage.getValues().size());
    }
    
    @Test
    public void insertSecond() {
        coverage = new SonarCoverage();
        coverage.linkFileNameToFileId("a", "1");
        coverageCache.merge(coverage,MODULE_NAME);
        coverage = new SonarCoverage();
        coverage.linkFileNameToFileId("b", "2");
        coverageCache.merge(coverage, MODULE_NAME);
        SonarCoverage coverageGotten = coverageCache.get(MODULE_NAME);
        assertNotNull("should be valid object",coverageGotten);
        assertEquals("should have one file",2,coverageGotten.getValues().size());
    }
}
