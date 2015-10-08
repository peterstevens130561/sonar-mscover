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
		LOG.info("Invoked");

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
	 * @return
	 */
    protected String onNotFound(MethodId methodId) {
        return null;
    }
    
}
