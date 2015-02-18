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

import static org.junit.Assert.*;

import org.junit.Test;

public class VsTestCoverageRepository_Merge_Test {

    private LineCoverageTestUtilities testUtilities = new LineCoverageTestUtilities();
    private VsTestCoverageRegistry emptyRepository = new VsTestCoverageRegistry("john");
    private VsTestCoverageRegistry populatedRepository = new VsTestCoverageRegistry("base");  
    @Test
    public void Merge_RepositoryIntoEmpty_ShouldMatchOriginal() {
        //Given file "statistics.cs" with coverage "1,0,1,0";
        int[] coverage = {1,0,1,0};
        givenFileWithCoverageData("statistics.cs", coverage);
        
        //When
        emptyRepository.merge(populatedRepository);
        
        //Then
        thenSourceFileNameTablesHasElements(1);
        thenFileIsAtIndex("statistics.cs",1);
        testUtilities.matchCoverage(emptyRepository.getSolutionLineCoverageData(),1,coverage);
    }
     

    
    @Test
    public void Merge_RepositoryIntoExisting_ShouldMatchOriginal() {
        //Given
        String name="statistics.cs";
        int[] coverage = {1,0,1,0};
        givenFileWithCoverageData(name, coverage);
        emptyRepository.merge(populatedRepository);
        
        populatedRepository = new VsTestCoverageRegistry("base"); 
        int[] coverage2 = {1,1,1,0};
        givenFileWithCoverageData(name, coverage2);
        
        //When
        emptyRepository.merge(populatedRepository);
        
        //Then
        thenSourceFileNameTablesHasElements(1);
        thenFileIsAtIndex("statistics.cs",1);
        int[] coverage3 = {2,1,2,0};
        testUtilities.matchCoverage(emptyRepository.getSolutionLineCoverageData(),1,coverage3);
    }
    
    
    @Test
    public void Merge_SecondFileRepositoryIntoExisting_ShouldMatchOriginal() {
        //Given
        String name="statistics.cs";
        int[] coverage = {1,0,1,0};
        String bogus = "bogus.cs";
        int[] coverageBogus = {1,1,1,1,0,0,0,0};
        givenFileWithCoverageData(name, coverage);
        givenFileWithCoverageData(bogus,coverageBogus);
        givenFileWithCoverageData("once.cs",coverageBogus);
        emptyRepository.merge(populatedRepository);
        

        
        populatedRepository = new VsTestCoverageRegistry("base"); 
        int[] coverage2 = {1,1,1,0};
        givenFileWithCoverageData(name, coverage2);
        int[] coverage3 = {0,0,0,0,0,0,0,1};
        givenFileWithCoverageData("bogus.cs", coverage3);       
        //When
        emptyRepository.merge(populatedRepository);
        
        //Then
        thenSourceFileNameTablesHasElements(3);
 
        thenFileIsAtIndex("statistics.cs",1);       
        int[] statExpected = {2,1,2,0};
        testUtilities.matchCoverage(emptyRepository.getSolutionLineCoverageData(),1,statExpected);
 
        thenFileIsAtIndex(bogus,2);     
        int[] bogusExpected = {1,1,1,1,0,0,0,1};
        testUtilities.matchCoverage(emptyRepository.getSolutionLineCoverageData(),2,bogusExpected);
        
    }
    private void thenFileIsAtIndex(String string, int i) {
        int fileIndex=emptyRepository.getSourceFileNameTable().getSourceFileId("statistics.cs");
        assertEquals("file should be at index 1", 1,fileIndex);
    }

    private void thenSourceFileNameTablesHasElements(int i) {
        assertEquals("SourceFileNameTable should have " + i + " element(s)",i,emptyRepository.getSourceFileNameTable().size());      
    }

    private int givenFileWithCoverageData(String name,int [] coverage) {
        int index=populatedRepository.getSourceFileNameTable().getSourceFileId(name);
        testUtilities.createCoverage(populatedRepository.getSolutionLineCoverageData(),index,coverage);  
        return index;
    }
}
