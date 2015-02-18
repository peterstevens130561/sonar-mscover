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

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SolutionLineCoverage;

public class TestCoverageRegistry implements SolutionLineCoverage {

    private int visitedFiles;
    private int coveredLines;
    private int uncoveredLines;

    public void addFile(int fileId, String filePath) {
        visitedFiles++;
    }

    public void addCoveredFileLine(int fileId, CoveragePoint point) {
        coveredLines++;       
    }

    public void save() {       
    }

    public int getFileCount() {
        return visitedFiles;
    }

    public Object getVisitedFiles() {
        return visitedFiles;
    }

    public Object getVisitedLines() {
        return coveredLines;
    }

    public int getLineCount() {
        return coveredLines;
    }

    public Collection<FileLineCoverage> getFileCoverages() {
        return null;
    }

    public void addUnCoveredFileLine(Integer sourceFileID, CoveragePoint point) {
        uncoveredLines++;
        
    }

    @Override
    public int getCoveredLineCount() {
        return coveredLines + uncoveredLines;
    }

    @Override
    public void merge(int destinationId, int toMergeId,
            SolutionLineCoverage lineCoverageRegistry) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public FileLineCoverage getFileLineCoverage(int sourceId) {
        // TODO Auto-generated method stub
        return null;
    }






}
