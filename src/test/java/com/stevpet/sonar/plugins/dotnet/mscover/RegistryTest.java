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
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceLine;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.DefaultSolutionLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestLinesToCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestSourceFileNamesToCoverageObserver;

public class RegistryTest {

    
    @Test 
    public void Parse_MSCoverage_Commits_CheckNoCoverage() throws XMLStreamException {
          //Arrange
        TestCoverageRegistry coverageRegistry = arrange("mscoverage.xml");
          
        //Act
          FileLineCoverage fileCoverage = coverageRegistry.getFileCoverageByName("Commits.cs");
          
          //Assert
          Assert.assertEquals(0,fileCoverage.getCoveredLines());
          Assert.assertEquals(24,getUncoveredLineCount(fileCoverage));
          Assert.assertEquals(24,fileCoverage.getCountLines());        
    }

    
    @Test 
    public void Parse_File2_NotCovered_CheckRegistry() throws XMLStreamException {
          //Arrange
        TestCoverageRegistry coverageRegistry = arrange("coverage.xml");
          
        //Act
          FileLineCoverage fileCoverage = coverageRegistry.getFileCoverageByName("Program.cs");
          
          //Assert
          Assert.assertEquals(0,fileCoverage.getCoveredLines());
          Assert.assertEquals(30,getUncoveredLineCount(fileCoverage));
          Assert.assertEquals(30,fileCoverage.getCountLines());        
    }
    
    @Test 
    public void Parse_ServerUriFinder_CheckCoverage() throws XMLStreamException {
          //Arrange
        TestCoverageRegistry coverageRegistry = arrange("coverage.xml");
          
        //Act
          FileLineCoverage fileCoverage = coverageRegistry.getFileCoverageByName("ServerUriFinder.cs");
          
          //Assert
          Assert.assertEquals(6,fileCoverage.getCoveredLines());
          Assert.assertEquals(3,getUncoveredLineCount(fileCoverage));
          Assert.assertEquals(9,fileCoverage.getCountLines());        
    }

    private TestCoverageRegistry arrange(String coverageName) throws XMLStreamException {
        File file = Helper.getResource(coverageName);
        XmlParserSubject parser = new CoverageParserSubject();
        
        TestCoverageRegistry coverageRegistry = new TestCoverageRegistry() ;
        VsTestLinesToCoverageObserver linesObserver = new VsTestLinesToCoverageObserver();
        linesObserver.setRegistry(coverageRegistry);
        parser.registerObserver(linesObserver);
        
        VsTestSourceFileNamesToCoverageObserver fileNamesObserver = new VsTestSourceFileNamesToCoverageObserver();
        fileNamesObserver.setRegistry(coverageRegistry);
        parser.registerObserver(fileNamesObserver);
        
        //Act

        parser.parseFile(file);
        return coverageRegistry;
    }


    private int getUncoveredLineCount(FileLineCoverage fileCoverage) {
        int uncovered=0;
        for(SourceLine line: fileCoverage.getLines().values()) {
            if(line.getVisits() == 0) {
                ++uncovered;
            }
        }
        return uncovered;
    }
    
    private class TestCoverageRegistry extends DefaultSolutionLineCoverage {

        public TestCoverageRegistry() {
            super("c:\\Users\\stevpet\\Documents\\GitHub\\tfsblame\\tfsblame");
        }
             
        public FileLineCoverage getFileCoverageByName(String name) {
            String lowerCaseName=name.toLowerCase();
            for(FileLineCoverage coverage : getRegistry().values()) {
                File file = coverage.getFile();
                if(file!=null && file.getName().toLowerCase().endsWith(lowerCaseName)) {
                    return coverage ;
                }
            }
            return null;
        }
        
    }
}
