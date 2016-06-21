package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarLinePoints;

public class CoverageSaverHelperTests {
    private CoverageSaverHelper coverageSaverHelper = new DefaultCoverageSaverHelper();
    
    @Before() 
    public void before() {
        
    }
    
    @Test
    public void empty() {
        CoverageLinePoints coveragePoints = new SonarLinePoints();     
        Metric<?> metric = CoreMetrics.COVERAGE_LINE_HITS_DATA;
        Measure<?> measure=coverageSaverHelper.getCoveredHitData(coveragePoints, metric);
        String values=measure.getData();
        String expected="";
        assertEquals(expected,values);
    }
    @Test
    public void simpleOrder() {
        CoverageLinePoints coveragePoints = new SonarLinePoints();
        coveragePoints.addPoint(1, true);
        coveragePoints.addPoint(2,true);
        coveragePoints.addPoint(10, true);
        coveragePoints.addPoint(11, true);
        coveragePoints.addPoint(100,true);
        coveragePoints.addPoint(101,true);       
        Metric<?> metric = CoreMetrics.COVERAGE_LINE_HITS_DATA;
        Measure<?> measure=coverageSaverHelper.getCoveredHitData(coveragePoints, metric);
        String values=measure.getData();
        String expected="1=1;2=1;10=1;11=1;100=1;101=1";
        assertEquals(expected,values);
    }
    
    @Test
    public void simpleOrderToCover() {
        CoverageLinePoints coveragePoints = new SonarLinePoints();
        coveragePoints.addPoint(1, true);
        coveragePoints.addPoint(2,true);
        coveragePoints.addPoint(10, true);
        coveragePoints.addPoint(11, true);
        coveragePoints.addPoint(100,true);
        coveragePoints.addPoint(101,true);       
        Metric<?> metric = CoreMetrics.COVERAGE_LINE_HITS_DATA;
        Measure<?> measure=coverageSaverHelper.getToCoverHitData(coveragePoints, metric);
        String values=measure.getData();
        String expected="1=1;2=1;10=1;11=1;100=1;101=1";
        assertEquals(expected,values);
    }
}
