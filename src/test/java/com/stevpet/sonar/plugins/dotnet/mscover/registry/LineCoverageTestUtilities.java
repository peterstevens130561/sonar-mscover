/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import org.junit.Assert;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceLine;

public class LineCoverageTestUtilities {
    public  SolutionLineCoverage createCoverage(int fileId,int [] coverage) {
        SolutionLineCoverage result = new DefaultSolutionLineCoverage("hi");
        createCoverage(result,fileId,coverage);
            return result;
        }

    public void createCoverage(SolutionLineCoverage result,
            int fileId, int[] coverage) {
        CoveragePoint point = new CoveragePoint();   
        for(int i=0;i<coverage.length;i++) {
            point.setStartLine(i);
            point.setEndLine(i);
            if(coverage[i]>0) {
                result.addCoveredFileLine(fileId, point);
            } else {
                result.addUnCoveredFileLine(fileId,point);
            }
        }
    }

    public void matchCoverage(SolutionLineCoverage solutionLineCoverageData,
            int fileId, int[] coverage) {
        FileLineCoverage actualCoverage=solutionLineCoverageData.getFileLineCoverage(fileId);
        Assert.assertNotNull("expected coverage data for file " + fileId,actualCoverage);
        Assert.assertEquals("number of lines differs",coverage.length,actualCoverage.getCountLines());
        int coveredLines=0;
        for(int i=0;i<coverage.length;i++) {
            SourceLine row = actualCoverage.getLines().get(i);
            Assert.assertEquals("coverage in line " + i, coverage[i],row.getVisits());
            coveredLines += coverage[i]>0?1:0;
        }
        Assert.assertEquals("coveredLines",coveredLines,actualCoverage.getCoveredLines());
        Assert.assertEquals("uncoveredLines",coverage.length-coveredLines,actualCoverage.getUncoveredLines());
    }

}
