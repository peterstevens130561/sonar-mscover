/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public class FileDrivenProjectUnitTestResultsService implements ProjectUnitTestResultsService {
    private final static Logger LOG = LoggerFactory.getLogger(DefaultProjectUnitTestResultsService.class);
    private VsTestResults unitTestingResults;
    private SourceFileNameTable sourceFileNamesTable;
    private MethodToSourceFileIdMap methodToSourceFileIdMap;
    Function<MethodId,String> onNotFoundResolver;

    public FileDrivenProjectUnitTestResultsService(VsTestResults unitTestingResults, MethodToSourceFileIdMap map,
            SourceFileNameTable sourceFileNamesTable, Function<MethodId, String> resolver) {
        this.unitTestingResults = unitTestingResults;
        this.sourceFileNamesTable = sourceFileNamesTable;
        this.methodToSourceFileIdMap=map;
        this.onNotFoundResolver=resolver;
    }
    @Override
    public ProjectUnitTestResults mapUnitTestResultsToFile() {
        return null;
    }

    /**
     * get all methods that 
     */
}
