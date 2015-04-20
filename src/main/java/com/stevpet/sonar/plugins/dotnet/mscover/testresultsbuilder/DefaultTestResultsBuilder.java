package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestingResults;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestResults;

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
    	

    	testResultsParser.parse(coverageFile);
    	UnitTestRegistry testResults = testResultsParser.getUnitTestRegistry();
    	
    	return mapUnitTestResultsToFile(testResults.getTestingResults(),methodToSourceFileIdMap,sourceFileNamesTable);
    }   
	
	public ProjectUnitTestResults mapUnitTestResultsToFile(UnitTestingResults unitTestingResults, MethodToSourceFileIdMap map,SourceFileNameTable sourceFileNamesTable) {
		Map<String,ClassUnitTestResult> unitTestFilesResultRegistry = new HashMap<String,ClassUnitTestResult>();

		Collection<UnitTestMethodResult>unitTests=unitTestingResults.values();
		for(UnitTestMethodResult unitTest:unitTests) {
			MethodId methodId=unitTest.getMethodId();
			String fileId = map.getLongestContainedMethod(methodId);
			if(fileId==null) {
				LOG.warn("Could not find fileId for " + methodId + " most likely lines were hidden (#hidden)");
				continue;
			}
			String filePath = sourceFileNamesTable.getSourceFileName(fileId);
			if(filePath==null) {
				LOG.warn("Could not find filename for method " + methodId + "");
				continue;
			}

			if(!unitTestFilesResultRegistry.containsKey(filePath)) {
				unitTestFilesResultRegistry.put(filePath, new ClassUnitTestResult(new File(filePath)));
			}
		}
		ProjectUnitTestResults projectUnitTestResults = new ProjectUnitTestResults();
		projectUnitTestResults.addAll(unitTestFilesResultRegistry.values());
		return projectUnitTestResults;
	}
    
}