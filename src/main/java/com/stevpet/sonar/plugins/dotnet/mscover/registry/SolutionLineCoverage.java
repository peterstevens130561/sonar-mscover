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

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;

public interface SolutionLineCoverage {

    /**
     * add a file to the coverage
     * @param fileId unique id of the file
     * @param sourceFilePath full path to the file
     */
    void addFile(int fileId, String sourceFilePath);

    /**
     *  Add point to the coverage registry for the given file
     * @param fileId unique id of the file
     * @param point coveragepoint of the line
     */
    void addCoveredFileLine(int fileId, CoveragePoint point);
    
     Collection<FileLineCoverage> getFileCoverages();
    

    int getFileCount();

    int getLineCount();

    int getCoveredLineCount();
    
    void addUnCoveredFileLine(Integer sourceFileID,
            CoveragePoint point);

    /**
     * merge the coverage info of the file with toMergeId Id of the sourceSoluctionLineCoverage, into destinationID
     * @param destinationId
     * @param toMergeId
     * @param lineCoverageRegistry
     */
    void merge(int destinationId, int toMergeId, SolutionLineCoverage lineCoverageRegistry);

    FileLineCoverage getFileLineCoverage(int sourceId);

}