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
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;

public class FileCoverageModelTest {
    private         int line=73;
    private ProjectCoverageRepository coverageModel;
    private SonarFileCoverage coveredFile;
    @Before
    public void before() {
         coverageModel = new DefaultProjectCoverageRepository();
         coveredFile = coverageModel.getCoverageOfFile("100"); 
    }
    
    @Test
    public void addOneLine_ShouldGetLine() {
        SonarLinePoint linePoint = createFirstLinePoint(line,true);      
        assertEquals(linePoint.getCovered()>0, true);
        assertEquals(linePoint.getLine(),73);
    }
    
    @Test
    public void visitOtherColumn_ShouldGetLowestCoverage() {      
        coveredFile.addLinePoint(line,false);
        CoverageLinePoint point=coveredFile.getLastLinePoint();
        assertEquals(point.getCovered()>0, false);
    }

    private SonarLinePoint createFirstLinePoint(int line,boolean isVisited) {
        return (SonarLinePoint)coveredFile.addLinePoint(line,isVisited);
        
    }

}
