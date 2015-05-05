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

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;

public class VsTestCoverageRegistry {
    
    private SourceFileNameTable sourceFileNamesTable = new SourceFileNameTable();
    private MethodToSourceFileIdMap methodToSourceFileIdMap = new MethodToSourceFileIdMap();
    private SolutionLineCoverage solutionLineCoverage ;

    public VsTestCoverageRegistry(String projectDirectory) {
        solutionLineCoverage = new DefaultSolutionLineCoverage(projectDirectory);
    }
    
    
    public SourceFileNameTable getSourceFileNameTable() {
        return sourceFileNamesTable;
    }
    
    public SolutionLineCoverage getSolutionLineCoverageData() {
        return solutionLineCoverage;
    }

    public MethodToSourceFileIdMap getMethodToSourceFileIdMap() {
        return methodToSourceFileIdMap;
    }
    
    public void merge(VsTestCoverageRegistry registryToMerge) {
        Collection<SourceFileNameRow> rows = registryToMerge.getSourceFileRows();
        for(SourceFileNameRow sourceFileRow: rows) {
            int toMergeId=sourceFileRow.getSourceFileID();
            String toMergeName=sourceFileRow.getSourceFileName();
            int destinationId=sourceFileNamesTable.getSourceFileId(toMergeName);
            solutionLineCoverage.merge(destinationId,toMergeId,registryToMerge.getSolutionLineCoverageData());
        }
    }


    private Collection<SourceFileNameRow> getSourceFileRows() {
        return sourceFileNamesTable.values();
    }
}
