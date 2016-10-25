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
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SpecFlowTestResultsBuilder implements TestResultsBuilder {

    private final Logger LOG = LoggerFactory.getLogger(SpecFlowTestResultsBuilder.class);
    private TestResultsBuilder testResultsBuilder ;
    public SpecFlowTestResultsBuilder(FileNamesParser fileNamesParser, TestResultsParser testResultsParser, ProjectUnitTestResultsService projectUnitTestResultsService) {
        testResultsBuilder = new DefaultTestResultsBuilder(fileNamesParser, testResultsParser, projectUnitTestResultsService);
    }

    public static SpecFlowTestResultsBuilder create (
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment
			) {
		VsTestResults unitTestingResults = new VsTestResults();
        MethodToSourceFileIdMap map = new MethodToSourceFileIdMap();
        SourceFileNameTable sourceFileNamesTable = new SourceFileNameTable();
        return new SpecFlowTestResultsBuilder (
				new OpenCoverFileNamesParser(map,sourceFileNamesTable), 
				new DefaultTestResultsParser(unitTestingResults), 
				new DefaultProjectUnitTestResultsService(unitTestingResults, map, sourceFileNamesTable,new SpecFlowScenarioMethodResolver(microsoftWindowsEnvironment))
				);
	}
   
    @Override
    public ProjectUnitTestResults getProjecttUnitTestResults(File testResultsFile, File coverageFile) {
        return testResultsBuilder.getProjecttUnitTestResults(testResultsFile, coverageFile);
    }

}
