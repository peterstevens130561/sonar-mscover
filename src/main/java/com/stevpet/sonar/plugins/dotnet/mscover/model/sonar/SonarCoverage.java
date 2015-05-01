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
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;


import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SonarCoverage {
    private Map<Integer,SonarFileCoverage> idMap = new HashMap<Integer,SonarFileCoverage>();
    // key = sourceFileName, value = id
    private Map<String,Integer> sourceFileNameMap = new HashMap<String,Integer>();
    private Integer maxFileId=0;

    /**
     * 
     * @param id of file
     * @return either a new FileCoverage, or the one found
     */
    public SonarFileCoverage getCoveredFile(String id) {
    	Integer fileId = Integer.parseInt(id);
        if( !idMap.containsKey(fileId)) {
            idMap.put(fileId, new SonarFileCoverage()) ;
        }
        maxFileId=fileId>maxFileId?fileId:maxFileId;
        return idMap.get(fileId);
    }
    
    /**
     * Create an entry to map a sourcefileName (key) to the fileId (value). 
     * If the entry already exists it is overwritten
     * @param sourceFileName
     * @param fileId
     */
    public void  linkFileNameToFileId(String sourceFileName,String fileId) {
    	sourceFileNameMap.put(sourceFileName, Integer.parseInt(fileId));
    	SonarFileCoverage fileCoverage=getCoveredFile(fileId);
    	fileCoverage.setAbsolutePath(sourceFileName);
    }

    public boolean fileIdExists(String fileID) {
    	Integer ID = Integer.parseInt(fileID);
    	return idMap.containsKey(ID);
    }
    public Collection<SonarFileCoverage> getValues() {
        return idMap.values();
    }

    /**
     * 
     * @return number of files listed in the coverage report
     */
    public int size() {
        return idMap.size();
    }

    /**
     * Merge the given repository into this repository
     * @param populatedRepository
     * @throws SonarCoverageMergeException in case a file is encountered that is already in the repository, but has a different number of lines
     */
	public void merge(SonarCoverage populatedRepository) {
		for(SonarFileCoverage fileCoverage : populatedRepository.getValues())   {
			mergeFile(fileCoverage);
		}
	}

	private void mergeFile(SonarFileCoverage fileCoverage) {
		String sourceFileName = fileCoverage.getAbsolutePath();
		CoverageLinePoints sourceCoverageLinePoints = fileCoverage.getLinePoints();
		List<CoverageLinePoint> sourceLinePoints = sourceCoverageLinePoints.getPoints();
		
		Integer destinationFileId = getFileIdInThisRepository(sourceFileName);
		// at this point we know that there is an entry in this repository for the sourceFile
		SonarFileCoverage destinationFileCoverage = idMap.get(destinationFileId);
		CoverageLinePoints destinationCoverageLinePoints = destinationFileCoverage.getLinePoints();
		List<CoverageLinePoint> destinationLinePoints = destinationCoverageLinePoints.getPoints();
		if(destinationLinePoints.size()==0) {
			mergeIntoEmptyDestination(sourceLinePoints, destinationFileCoverage);
			return;
		}
		if(destinationLinePoints.size() != sourceLinePoints.size()) {
			throw new SonarCoverageException("Merging failed due to difference in number of linepoints ");
		}
		mergeIntoDestinationWithSamesSize(sourceLinePoints,
				destinationLinePoints);
		
		
	}

	/**
	 * get the fileId in this repo
	 * @param sourceFileName
	 * @return id
	 */
	private Integer getFileIdInThisRepository(String sourceFileName) {
		Integer destinationFileId = sourceFileNameMap.get(sourceFileName);
		if(destinationFileId==null) {
			maxFileId +=1;
			destinationFileId = maxFileId;
			linkFileNameToFileId(sourceFileName,maxFileId.toString());
		}
		return destinationFileId;
	}

	private void mergeIntoDestinationWithSamesSize(
			List<CoverageLinePoint> sourceLinePoints,
			List<CoverageLinePoint> destinationLinePoints) {
		for(int index=0;index< sourceLinePoints.size();index++) {
			CoverageLinePoint linePoint=sourceLinePoints.get(index);
			int line=linePoint.getLine();
			int covered = linePoint.getCovered();
			if(covered>0) {
				destinationLinePoints.get(index).setCovered(covered);
			}
		}
	}

	private void mergeIntoEmptyDestination(
			List<CoverageLinePoint> sourceLinePoints,
			SonarFileCoverage destinationFileCoverage) {
		for(int index=0;index<sourceLinePoints.size();index++) {
			CoverageLinePoint linePoint=sourceLinePoints.get(index);
			int line=linePoint.getLine();
			int covered = linePoint.getCovered();
			destinationFileCoverage.addLinePoint(line, covered>0);
		}
		return;
	}

	
}
