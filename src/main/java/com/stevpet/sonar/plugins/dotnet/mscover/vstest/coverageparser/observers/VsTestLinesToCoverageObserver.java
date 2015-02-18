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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;



import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SolutionLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;

public class VsTestLinesToCoverageObserver extends VsTestCoverageObserver {
    private SolutionLineCoverage coverageRegistry;
    private CoveragePoint coveragePoint;
    private boolean covered;

    public VsTestLinesToCoverageObserver() {
        setPattern("Module/NamespaceTable/Class/Method/Lines/(LnStart|LnEnd|Coverage|SourceFileID)");
    }

    public void setRegistry(SolutionLineCoverage coverageRegistry) {
        this.coverageRegistry=coverageRegistry;     
    }
    
    @Override
    public void setVsTestRegistry(VsTestCoverageRegistry vsTestRegistry) {
        this.coverageRegistry=vsTestRegistry.getSolutionLineCoverageData();      
    }
    
    @ElementMatcher(elementName="LnStart")
    public void lnStartMatcher(String lnStart) {
        coveragePoint = new CoveragePoint();
        coveragePoint.setStartLine(Integer.parseInt(lnStart));
    }
    
    @ElementMatcher(elementName="LnEnd")
    public void lnEndMatcher(String lnEnd) {
        coveragePoint.setEndLine(Integer.parseInt(lnEnd));
    }
    
    @ElementMatcher(elementName="Coverage" )
    public void coverageMatcher(String coverage) {
        covered = "0".equals(coverage);
        int visits= covered?1:0;
        coveragePoint.setCountVisits(visits);
    }
    
    @ElementMatcher(elementName="SourceFileID") 
    public void sourceFileIdMatcher(String value) {
        int sourceFileID=Integer.parseInt(value);
        if(covered) {
            coverageRegistry.addCoveredFileLine(sourceFileID,coveragePoint);
        } else {
            coverageRegistry.addUnCoveredFileLine(sourceFileID,coveragePoint);
        }
    }


    
}
