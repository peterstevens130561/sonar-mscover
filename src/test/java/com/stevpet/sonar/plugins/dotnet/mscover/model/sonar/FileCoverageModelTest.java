package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class FileCoverageModelTest {
    private         int line=73;
    private SonarCoverage coverageModel;
    private SonarFileCoverage coveredFile;
    @Before
    public void before() {
         coverageModel = new SonarCoverage();
         coveredFile = coverageModel.getCoveredFile("100"); 
    }
    
    @Test
    public void addOneLine_ShouldGetLine() {

        SonarLinePoint linePoint = createFirstLinePoint(line,true);
        
        assertEquals(linePoint.isVisited(), true);
        assertEquals(linePoint.getLine(),73);
    }
    
    @Test
    public void visitOtherColumn_ShouldGetLowestCoverage() {
        CoveragePoint linePoint= createFirstLinePoint(line,true);
        
        coveredFile.addLinePoint(line,false);
        SonarLinePoint point=coveredFile.getLastLinePoint();
        assertEquals(point.isVisited(), false);
    }

    private SonarLinePoint createFirstLinePoint(int line,boolean isVisited) {
        return coveredFile.addLinePoint(line,isVisited);
        
    }

}
