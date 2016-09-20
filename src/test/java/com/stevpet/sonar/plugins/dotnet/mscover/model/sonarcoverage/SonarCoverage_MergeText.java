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
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonarcoverage;

import static org.junit.Assert.assertEquals;
import java.util.List;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class SonarCoverage_MergeText {

    private ProjectCoverageRepository destinationRepository = new DefaultProjectCoverageRepository();
    private ProjectCoverageRepository populatedRepository = new DefaultProjectCoverageRepository();  
    @Test
    public void Merge_RepositoryIntoEmpty_ShouldMatchOriginal() {
        //Given file "statistics.cs" with coverage "1,0,1,0";
        int[] coverage = {1,0,1,0};
        givenFileWithCoverageData("statistics.cs", "1", coverage);
        
        merge();
        
        //Then
        thenSourceFileNameTablesHasElements(1);
        matchCoverage(destinationRepository,"1",coverage);
    }
  
    @Test
    public void Merge_SameFileDifferentNumberOfLines_ExpectExcetion() {
        String name="statistics.cs";
        int[] coverage = {1,0,1,0};
        givenFileWithCoverageData(name, "1", coverage);
        merge();
        
        startBuildingCoverageFile();  

        int[] secondCoverage = {1,1,1};
        givenFileWithCoverageData(name, "1", secondCoverage);
        
        merge();
        thenSourceFileNameTablesHasElements(1);
        int[] coverage3 = {2,1,2,0};
        matchCoverage(destinationRepository,"1",coverage3);        
        
    }
    

	private void startBuildingCoverageFile() {
		populatedRepository = new DefaultProjectCoverageRepository();
	}

	private void merge() {
		destinationRepository.mergeIntoThis(populatedRepository);
	}
	@Test
    public void Merge_RepositoryIntoExisting_ShouldMatchOriginal() {
        //Given
        String name="statistics.cs";
        int[] coverage = {1,0,1,0};
        givenFileWithCoverageData(name, "1", coverage);
        merge();
        
        startBuildingCoverageFile(); 
        int[] coverage2 = {1,1,1,0};
        givenFileWithCoverageData(name, "1", coverage2);
        
        merge();
        
        //Then
        thenSourceFileNameTablesHasElements(1);
        int[] coverage3 = {2,1,2,0};
        matchCoverage(destinationRepository,"1",coverage3);
    }
    
	   @Test
	    public void merge_DifferntLinesl() {
	        //Given
	        String name="statistics.cs";
	        int[] coverage = {1,0,1,0};
	        int[] lines = {10,11,12,13};
	        givenFileWithCoverageData(name, "1", coverage,lines);
	        merge();
	        
	        startBuildingCoverageFile(); 
	        int[] coverage2 = {1,1,1,0,1};
	        int[] lines2 = {1,2,12,13,16};
	        givenFileWithCoverageData(name, "1", coverage2,lines2);
	        
	        merge();
	        
	        //Then
	        thenSourceFileNameTablesHasElements(1);
	        int[] coverage3 = {1,1,1,0,2,0,1};
	        int[] lines3= {1,2,10,11,12,13,16};
	        matchCoverage(destinationRepository,"1",coverage3,lines3);
	    }
  




    @Test
    public void Merge_SecondFileRepositoryIntoExisting_ShouldHaveThreeFiles() {
        //Given
        setupRepoWith3FilesThenMerge2FilesWithNamesInFirst();
        
        //Then
        thenSourceFileNameTablesHasElements(3);
            
    }
    @Test
    public void Merge_SecondFileRepositoryIntoExisting_FirstFileMatch() {
        //Given
        setupRepoWith3FilesThenMerge2FilesWithNamesInFirst();
        
       
        int[] statExpected = {2,1,2,0};
        matchCoverage(destinationRepository,"1",statExpected);        
    }

    @Test
    public void Merge_SecondFileRepositoryIntoExisting_SecondFileMatch() {
        //Given
        setupRepoWith3FilesThenMerge2FilesWithNamesInFirst();
        
        //Then
        thenSourceFileNameTablesHasElements(3);
       
        int[] bogusExpected = {1,1,1,1,0,0,0,1};
        matchCoverage(destinationRepository,"2",bogusExpected);
        
    }
	private void setupRepoWith3FilesThenMerge2FilesWithNamesInFirst() {
		String name="statistics.cs";
        int[] coverage = {1,0,1,0};
        String bogus = "bogus.cs";
        int[] coverageBogus = {1,1,1,1,0,0,0,0};
        givenFileWithCoverageData(name, "1", coverage);
        givenFileWithCoverageData(bogus,"2", coverageBogus);
        givenFileWithCoverageData("once.cs","3",coverageBogus);
        merge();
        

        
        startBuildingCoverageFile(); 
        int[] coverage2 = {1,1,1,0};
        givenFileWithCoverageData(name, "1",coverage2);
        int[] coverage3 = {0,0,0,0,0,0,0,1};
        givenFileWithCoverageData(bogus, "2", coverage3);       
        merge();
	}


    private void thenSourceFileNameTablesHasElements(int i) {
        assertEquals("SourceFileNameTable should have " + i + " element(s)",i,destinationRepository.size());      
    }

    private void givenFileWithCoverageData(String name,String id, int [] coverage) {
    	SonarFileCoverage fileCoverage=populatedRepository.getCoverageOfFile(id); 
    	for(int i=0;i<coverage.length;i++) {
    		fileCoverage.addLinePoint(i, coverage[i]>0);
    	}
    	populatedRepository.linkFileNameToFileId(name, id);
    }
 
    private void givenFileWithCoverageData(String name, String id, int[] coverage, int[] lines) {
        SonarFileCoverage fileCoverage=populatedRepository.getCoverageOfFile(id); 
        for(int i=0;i<coverage.length;i++) {
            fileCoverage.addLinePoint(lines[i], coverage[i]>0);
        }
        populatedRepository.linkFileNameToFileId(name, id);
    }
    
    private void matchCoverage(ProjectCoverageRepository emptyRepository2, String fileID,
			int[] coverage) {
    	SonarFileCoverage fileCoverage=destinationRepository.getCoverageOfFile(fileID);
    	for(CoverageLinePoint linePoint: fileCoverage.getLinePoints().getPoints()) {
    		int line=linePoint.getLine();
    		int covered=linePoint.getCovered();
    		assertEquals("line " + line,coverage[line]>0,covered>0);
    	}
	}
    
    private void matchCoverage(ProjectCoverageRepository destinationRepository2, String fileID, int[] coverage, int[] lines3) {
        SonarFileCoverage fileCoverage=destinationRepository.getCoverageOfFile(fileID);
        List<CoverageLinePoint> points = fileCoverage.getLinePoints().getPoints();
        assertEquals("size should be same",lines3.length,points.size());
        for(int i=0;i<points.size();i++) {
            CoverageLinePoint linePoint = points.get(i);
            int covered=linePoint.getCovered();
            assertEquals("line at " + i,lines3[i],linePoint.getLine());
            assertEquals("coverage at " + i,coverage[i]>0,covered>0);
        }
    }
    

}