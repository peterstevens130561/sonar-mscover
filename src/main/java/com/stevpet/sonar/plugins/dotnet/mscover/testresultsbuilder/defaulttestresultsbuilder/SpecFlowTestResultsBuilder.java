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
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverFileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SpecFlowTestResultsBuilder extends DefaultTestResultsBuilder {

    private final Logger LOG = LoggerFactory.getLogger(SpecFlowTestResultsBuilder.class);
    private SpecFlowScenarioMethodResolver specFlowScenarioMethodResolver;

    public SpecFlowTestResultsBuilder(FileNamesParser fileNamesParser, TestResultsParser testResultsParser,SpecFlowScenarioMethodResolver specFlowScenarioMethodResolver, ProjectUnitTestResultsService projectUnitTestResultsService) {
        super(fileNamesParser, testResultsParser, projectUnitTestResultsService);
        this.specFlowScenarioMethodResolver = specFlowScenarioMethodResolver;
    }

    public static TestResultsBuilder create (
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment
			) {
		VsTestResults unitTestingResults = new VsTestResults();
        MethodToSourceFileIdMap map = new MethodToSourceFileIdMap();
        SourceFileNameTable sourceFileNamesTable = new SourceFileNameTable();
        return new SpecFlowTestResultsBuilder (
				new OpenCoverFileNamesParser(map,sourceFileNamesTable), 
				new DefaultTestResultsParser(unitTestingResults), 
				new SpecFlowScenarioMethodResolver(microsoftWindowsEnvironment), 
				new DefaultProjectUnitTestResultsService(unitTestingResults, map, sourceFileNamesTable)
				);
	}
   
    //@Override
    protected String onNotFound(MethodId methodId) {
        String methodName=methodId.getMethodName();
        File  file=specFlowScenarioMethodResolver.getFile(methodName);
        if(file==null) {
            LOG.warn("Tried to resolve a potential specflow method, but failed {}",methodName);
        }
        return file==null?null:file.getAbsolutePath();
        
    }
}
