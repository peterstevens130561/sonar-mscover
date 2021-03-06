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

import java.io.File;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;

public class DefaultTestResultsBuilder implements TestResultsBuilder {

	private final FileNamesParser fileNamesParser;
	private final TestResultsParser testResultsParser;
	private final ProjectUnitTestResultsService projectUnitTestResultsService;
	
    public DefaultTestResultsBuilder(FileNamesParser fileNamesParser,TestResultsParser testResultsParser,ProjectUnitTestResultsService projectUnitTestResultsService) {
    	this.fileNamesParser = fileNamesParser;
    	this.testResultsParser = testResultsParser;
    	this.projectUnitTestResultsService = projectUnitTestResultsService;
    }
 
	@Override
	public ProjectUnitTestResults getProjecttUnitTestResults(File testResultsFile, File coverageFile) {
    	fileNamesParser.parse(coverageFile);  	
    	testResultsParser.parse(testResultsFile);
    	ProjectUnitTestResults results = projectUnitTestResultsService.mapUnitTestResultsToFile();
    	return results;
    }
	
}
