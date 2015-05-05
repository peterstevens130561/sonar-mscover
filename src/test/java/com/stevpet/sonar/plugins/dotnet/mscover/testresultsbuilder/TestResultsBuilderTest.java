package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;

public class TestResultsBuilderTest {
	private TestResultsBuilder testResultsBuilder ;
	private FileNamesParserMock fileNamesParserMock = new FileNamesParserMock();
	private TestResultsParserMock testResultsParserMock = new TestResultsParserMock();
	
    private UnitTestRegistry testResults;
    private SourceFileNameTable sourceFileNamesTable;

    private MethodToSourceFileIdMap methodToSourceFileIdMap;
	private String CLASS = "TestingClass";
	private String MODULE = "Module.dll";
	private String NAMESPACE = "BHI.FUN";
	private String FULLCLASSNAME = NAMESPACE + "." + CLASS;
    
	@Before() 
	public void before() {
		methodToSourceFileIdMap = new MethodToSourceFileIdMap();
		testResults = new UnitTestRegistry();
		testResultsParserMock.givenTestResults(testResults);
		testResultsBuilder = new DefaultTestResultsBuilder(fileNamesParserMock.getMock(), testResultsParserMock.getMock());
		sourceFileNamesTable = new SourceFileNameTable();
		fileNamesParserMock.givenSourceFileNamesTable(sourceFileNamesTable);
		fileNamesParserMock.givenGetMethodToSourceFileIdMap(methodToSourceFileIdMap);
	}
	
	@Test
	public void NoTestResults_ShouldHaveEmptyResults() {
		ProjectUnitTestResults projectUnitTestResults=testResultsBuilder.parse(null, null);
		assertNotNull("parse should always return valid object",projectUnitTestResults);
		assertEquals("no results expected",0,projectUnitTestResults.values().size());
	}
	
	@Test
	public void OneFileWithResult_ShowHaveInFile() {
		methodToSourceFileIdMap.add(new MethodId(MODULE,NAMESPACE,CLASS,"method1"), "1");
	
		sourceFileNamesTable.getNewRow("1").setSourceFileName("myname");
	
		UnitTestingResults results=testResults.getTestingResults();

		results.newEntry().setClassName(FULLCLASSNAME)
			.setNamespaceNameFromClassName(FULLCLASSNAME)
			.setModuleFromCodeBase(MODULE)
			.setTestId("id1")
			.setTestName("method1").addToParent();
	
		ProjectUnitTestResults projectUnitTestResults=testResultsBuilder.parse(null, null);
		assertNotNull("parse should always return valid object",projectUnitTestResults);
		assertEquals("One results expected",1,projectUnitTestResults.values().size());
	}
	
	@Test
	public void OneMethodInCoverage_DifferentTestMethod_ShouldNotFind() {

		methodToSourceFileIdMap.add(new MethodId(MODULE,NAMESPACE,CLASS,"method1"), "1");
		fileNamesParserMock.givenGetMethodToSourceFileIdMap(methodToSourceFileIdMap);
		
		sourceFileNamesTable.getNewRow("1").setSourceFileName("myname");
		
		UnitTestingResults results=testResults.getTestingResults();
		results.newEntry().setClassName(FULLCLASSNAME)
			.setNamespaceNameFromClassName(FULLCLASSNAME)
			.setModuleFromCodeBase(MODULE)
			.setTestName("bogus").addToParent();

		ProjectUnitTestResults projectUnitTestResults=testResultsBuilder.parse(null, null);
		assertNotNull("parse should always return valid object",projectUnitTestResults);
		assertEquals("No results expected",0,projectUnitTestResults.values().size());
	}
	
}
