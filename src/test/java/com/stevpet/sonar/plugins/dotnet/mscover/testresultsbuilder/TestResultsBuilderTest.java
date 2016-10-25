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
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.DefaultProjectUnitTestResultsService;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.DefaultTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.ProjectUnitTestResultsService;

public class TestResultsBuilderTest {
	private TestResultsBuilder testResultsBuilder ;
	private FileNamesParserMock fileNamesParserMock = new FileNamesParserMock();
	@Mock private TestResultsParser testResultsParser;
	
    private VsTestResults testResults;
    private SourceFileNameTable sourceFileNamesTable;

    private MethodToSourceFileIdMap methodToSourceFileIdMap;
	private String CLASS = "TestingClass";
	private String MODULE = "Module.dll";
	private String NAMESPACE = "BHI.FUN";
	private String FULLCLASSNAME = NAMESPACE + "." + CLASS;
    private ProjectUnitTestResultsService projectUnitTestResultsService ;
    
	@Before() 
	public void before() {
	    org.mockito.MockitoAnnotations.initMocks(this);
		methodToSourceFileIdMap = new MethodToSourceFileIdMap();
		testResults = new VsTestResults();
		sourceFileNamesTable = new SourceFileNameTable();
		projectUnitTestResultsService = new DefaultProjectUnitTestResultsService(testResults, methodToSourceFileIdMap, sourceFileNamesTable);
		testResultsBuilder = new DefaultTestResultsBuilder(fileNamesParserMock.getMock(), testResultsParser, projectUnitTestResultsService);
		fileNamesParserMock.givenSourceFileNamesTable(sourceFileNamesTable);
		fileNamesParserMock.givenGetMethodToSourceFileIdMap(methodToSourceFileIdMap);
	}
	
	@Test
	public void NoTestResults_ShouldHaveEmptyResults() {
		ProjectUnitTestResults projectUnitTestResults=testResultsBuilder.getProjecttUnitTestResults(null, null);
		assertNotNull("parse should always return valid object",projectUnitTestResults);
		assertEquals("no results expected",0,projectUnitTestResults.values().size());
	}
	
	@Test
	public void OneFileWithResult_ShowHaveInFile() {
		methodToSourceFileIdMap.add(new MethodId(MODULE,NAMESPACE,CLASS,"method1"), "1");
	
		sourceFileNamesTable.getNewRow("1").setSourceFileName("myname");

		testResults.add("SOMEID").setClassName(FULLCLASSNAME)
			.setNamespaceNameFromClassName(FULLCLASSNAME)
			.setModuleFromCodeBase(MODULE)
			.setOutcome("Failed")
			.setTestName("method1");
	
		ProjectUnitTestResults projectUnitTestResults=testResultsBuilder.getProjecttUnitTestResults(null, null);
		assertNotNull("parse should always return valid object",projectUnitTestResults);
		assertEquals("One results expected",1,projectUnitTestResults.values().size());
		ClassUnitTestResult unitTestClass = projectUnitTestResults.values().iterator().next();
		assertEquals(new File("myname"),unitTestClass.getFile());
	}
	
	@Test
	public void OneMethodInCoverage_DifferentTestMethod_ShouldNotFind() {

		methodToSourceFileIdMap.add(new MethodId(MODULE,NAMESPACE,CLASS,"method1"), "1");
		fileNamesParserMock.givenGetMethodToSourceFileIdMap(methodToSourceFileIdMap);
		
		sourceFileNamesTable.getNewRow("1").setSourceFileName("myname");
		
		VsTestResults results=testResults;
		results.add("SOMEID").setClassName(FULLCLASSNAME)
			.setNamespaceNameFromClassName(FULLCLASSNAME)
			.setModuleFromCodeBase(MODULE)
			.setTestName("bogus");

		ProjectUnitTestResults projectUnitTestResults=testResultsBuilder.getProjecttUnitTestResults(null, null);
		assertNotNull("parse should always return valid object",projectUnitTestResults);
		assertEquals("No results expected",0,projectUnitTestResults.values().size());
	}
	
}
