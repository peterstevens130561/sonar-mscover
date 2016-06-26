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


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

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
    
    @Override
    public boolean equals(Object other) {
        if( other == null ) {
            return false;
        }
        SonarCoverage otherCoverage = (SonarCoverage) other ;
        boolean same=idMap.equals(otherCoverage.idMap) && 
                sourceFileNameMap.equals(otherCoverage.sourceFileNameMap) && 
                maxFileId.equals(otherCoverage.maxFileId);
        return same;
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
		     SonarFileCoverage destinationFileCoverage = getFileCoverageToMergeInto(fileCoverage);
		     destinationFileCoverage.merge(fileCoverage);
		}
	}


    private SonarFileCoverage getFileCoverageToMergeInto(
            SonarFileCoverage fileCoverage) {
        String sourceFileName = fileCoverage.getAbsolutePath();
        Preconditions.checkArgument(StringUtils.isNotEmpty(sourceFileName),"no sourcefilename for coverage data");
		Integer destinationFileId = getFileIdInThisRepository(sourceFileName);
		SonarFileCoverage destinationFileCoverage = idMap.get(destinationFileId);
        return destinationFileCoverage;
    }

	/**
	 * get the fileId in this repo for the given source
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

	
}
