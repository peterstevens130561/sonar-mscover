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
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.MethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;

public class DefaultTestResultsBuilder implements TestResultsBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTestResultsBuilder.class);


	//private final FileNamesParser fileNamesParser;
	private final TestResultsParser testResultsParser;


    private MethodRepository methodRepository ;


    private SourceFileRepository sourceFileRepository;


    private UnitTestingResults testResults;
	
    public DefaultTestResultsBuilder(TestResultsParser testResultsParser,MethodRepository methodRepository, SourceFileRepository sourceFileRepository,UnitTestingResults unitTestingResults) {
    	//this.fileNamesParser = fileNamesParser;
    	this.testResultsParser = testResultsParser;
    	this.methodRepository = methodRepository;
    	this.sourceFileRepository=sourceFileRepository;
    	this.testResults=unitTestingResults;
    }
    
 
	@Override
	public ProjectUnitTestResults getTestResults() {
    	
    	return mapUnitTestResultsToFile(testResults,methodRepository,sourceFileRepository);
    }   
	
	public ProjectUnitTestResults mapUnitTestResultsToFile(UnitTestingResults unitTestingResults, MethodRepository map,SourceFileRepository sourceFileRepository) {
		Map<String,ClassUnitTestResult> unitTestFilesResultRegistry = new HashMap<String,ClassUnitTestResult>();

		Collection<UnitTestMethodResult>unitTests=unitTestingResults.values();
		for(UnitTestMethodResult unitTest:unitTests) {
			MethodId methodId=unitTest.getMethodId();
			String fileId = map.getFileId(methodId);
			String filePath=null;
			if(fileId!=null) {
		         filePath = sourceFileRepository.getSourceFileName(fileId);
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


    @Override
    public void parseCoverage(File coverageFile) {
        //fileNamesParser.parse(coverageFile);
        //methodRepository = fileNamesParser.getMethodToSourceFileIdMap();
        //sourceFileRepository = fileNamesParser.getSourceFileRepository();
    }


    @Override
    public void parseTestResults(File testResultsFile) {
        testResultsParser.parse(testResultsFile);
        //testResults = testResultsParser.getUnitTestRegistry();
    }
    
}
