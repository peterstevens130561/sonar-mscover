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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;

public class DefaultTestResultsBuilder implements TestResultsBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTestResultsBuilder.class);


	private final FileNamesParser fileNamesParser;
	private final TestResultsParser testResultsParser;
	
    public DefaultTestResultsBuilder(FileNamesParser fileNamesParser,TestResultsParser testResultsParser) {
    	this.fileNamesParser = fileNamesParser;
    	this.testResultsParser = testResultsParser;
    }
    
 
	@Override
	public ProjectUnitTestResults parse(File testResultsFile, File coverageFile) {

    	fileNamesParser.parse(coverageFile);
    	MethodToSourceFileIdMap methodToSourceFileIdMap=fileNamesParser.getMethodToSourceFileIdMap();
    	SourceFileNameTable sourceFileNamesTable= fileNamesParser.getSourceFileNamesTable();
    	

    	testResultsParser.parse(testResultsFile);
    	UnitTestRegistry testResults = testResultsParser.getUnitTestRegistry();
    	
    	return mapUnitTestResultsToFile(testResults.getTestingResults(),methodToSourceFileIdMap,sourceFileNamesTable);
    }   
	
	public ProjectUnitTestResults mapUnitTestResultsToFile(UnitTestingResults unitTestingResults, MethodToSourceFileIdMap map,SourceFileNameTable sourceFileNamesTable) {
		Map<String,ClassUnitTestResult> unitTestFilesResultRegistry = new HashMap<String,ClassUnitTestResult>();

		Collection<UnitTestMethodResult>unitTests=unitTestingResults.values();
		for(UnitTestMethodResult unitTest:unitTests) {
			MethodId methodId=unitTest.getMethodId();
			String fileId = map.get(methodId);
			String filePath=null;
			if(fileId!=null) {
		         filePath = sourceFileNamesTable.getSourceFileName(fileId);
			}

			if (filePath==null) {
			    filePath=onNotFound(methodId);
			}
			if(filePath==null) {
				LOG.warn("Could not find filename for method " + methodId + "");
				continue;
			}

			if(!unitTestFilesResultRegistry.containsKey(filePath)) {
				unitTestFilesResultRegistry.put(filePath, new ClassUnitTestResult(new File(filePath)));
			}
			ClassUnitTestResult classUnitTestResult=unitTestFilesResultRegistry.get(filePath);
			classUnitTestResult.add(unitTest);
			
		}
		ProjectUnitTestResults projectUnitTestResults = new ProjectUnitTestResults();
		projectUnitTestResults.addAll(unitTestFilesResultRegistry.values());
		return projectUnitTestResults;
	}


	/**
	 * extension point to override normal behavior
	 * @param methodId
	 * @return absolute path to the file
	 */
    protected String onNotFound(MethodId methodId) {
        return null;
    }
    
}
