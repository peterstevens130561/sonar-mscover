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


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;
/**
 * Repository for all coverage data in a sonar project
 * @author stevpet
 *
 */
public class DefaultProjectCoverageRepository implements ProjectCoverageRepository {

    private static int INITIAL_SIZE = 4096;
    private Map<Integer,SonarFileCoverage> idMap = new HashMap<Integer,SonarFileCoverage>(INITIAL_SIZE);
    // key = sourceFileName, value = id
    private Map<String,Integer> sourceFileNameMap = new HashMap<String,Integer>(INITIAL_SIZE);
    private Integer maxFileId=0;

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository#getCoverageOfFile(java.lang.String)
     */
    @Override
    public SonarFileCoverage getCoverageOfFile(String id) {
    	Integer fileId = Integer.parseInt(id);
        if( !idMap.containsKey(fileId)) {
            idMap.put(fileId, new SonarFileCoverage()) ;
        }
        maxFileId=fileId>maxFileId?fileId:maxFileId;
        return idMap.get(fileId);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if( other == null ) {
            return false;
        }
        DefaultProjectCoverageRepository otherCoverage = (DefaultProjectCoverageRepository) other ;
        boolean same=idMap.equals(otherCoverage.idMap) && 
                sourceFileNameMap.equals(otherCoverage.sourceFileNameMap) && 
                maxFileId.equals(otherCoverage.maxFileId);
        return same;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository#fileIdExists(java.lang.String)
     */
    @Override
    public boolean fileIdExists(String fileID) {
    	Integer ID = Integer.parseInt(fileID);
    	return idMap.containsKey(ID);
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository#getValues()
     */
    @Override
    public Collection<SonarFileCoverage> getValues() {
        return idMap.values();
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository#size()
     */
    @Override
    public int size() {
        return idMap.size();
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository#mergeIntoThis(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository)
     */
	@Override
    public void mergeIntoThis(ProjectCoverageRepository otherRepository) {
		for(SonarFileCoverage fileCoverage : otherRepository.getValues())   {
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

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository#linkFileNameToFileId(java.lang.String, java.lang.String)
     */
    @Override
    public void  linkFileNameToFileId(String sourceFileName,String fileId) {
        sourceFileNameMap.put(sourceFileName, Integer.parseInt(fileId));
        SonarFileCoverage fileCoverage=getCoverageOfFile(fileId);
        fileCoverage.setAbsolutePath(sourceFileName);
    }

    @Override
    public void addBranchPoint(String fileId, int line, int path, boolean visited) {
        SonarFileCoverage coveragePoints = getCoverageOfFile(fileId);
        coveragePoints.addBranchPoint(line, path, visited);
    }

    @Override
    public void addLinePoint(String fileId, int line, boolean visited) {
        SonarFileCoverage coveragePoints = getCoverageOfFile(fileId);
        coveragePoints.addLinePoint(line,visited);
    }

	
}
