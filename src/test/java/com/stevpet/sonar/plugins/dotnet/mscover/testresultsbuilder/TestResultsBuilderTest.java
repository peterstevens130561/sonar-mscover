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

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultSourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.DefaultTestResultsBuilder;
import static org.mockito.Mockito.when;
public class TestResultsBuilderTest {
	private TestResultsBuilder testResultsBuilder ;
	@Mock private FileNamesParser fileNamesParser ; 
	private TestResultsParserMock testResultsParserMock = new TestResultsParserMock();
	
    private UnitTestRegistry testResults;
    private SourceFileRepository sourceFileNamesTable;

    private MethodToSourceFileIdRepository methodToSourceFileIdMap;
	private String CLASS = "TestingClass";
	private String MODULE = "Module.dll";
	private String NAMESPACE = "BHI.FUN";
	private String FULLCLASSNAME = NAMESPACE + "." + CLASS;
    
	@Before() 
	public void before() {
		methodToSourceFileIdMap = new MethodToSourceFileIdRepository();
		testResults = new UnitTestRegistry();
		testResultsParserMock.givenTestResults(testResults);
		
		org.mockito.MockitoAnnotations.initMocks(this);
		
		testResultsBuilder = new DefaultTestResultsBuilder(fileNamesParser, testResultsParserMock.getMock(), methodToSourceFileIdMap, sourceFileNamesTable);
		sourceFileNamesTable = new DefaultSourceFileRepository();
		when(fileNamesParser.getMethodToSourceFileIdMap()).thenReturn(methodToSourceFileIdMap);
		when(fileNamesParser.getSourceFileRepository()).thenReturn(sourceFileNamesTable);
	}
	
	@Test
	public void NoTestResults_ShouldHaveEmptyResults() {
	    ProjectUnitTestResults projectUnitTestResults = parse();
		assertNotNull("parse should always return valid object",projectUnitTestResults);
		assertEquals("no results expected",0,projectUnitTestResults.values().size());
	}
	
	@Test
	public void OneFileWithResult_ShowHaveInFile() {
		methodToSourceFileIdMap.add("1", new MethodId(MODULE,NAMESPACE,CLASS,"method1"));
	
		sourceFileNamesTable.add("1", "myname");
		UnitTestingResults results=testResults.getTestingResults();

		results.newEntry().setClassName(FULLCLASSNAME)
			.setNamespaceNameFromClassName(FULLCLASSNAME)
			.setModuleFromCodeBase(MODULE)
			.setTestId("id1")
			.setOutcome("Failed")
			.setTestName("method1").addToParent();
	
	      ProjectUnitTestResults projectUnitTestResults = parse();
		assertNotNull("parse should always return valid object",projectUnitTestResults);
		assertEquals("One results expected",1,projectUnitTestResults.values().size());
	}
	
	@Test
	public void OneMethodInCoverage_DifferentTestMethod_ShouldNotFind() {

		methodToSourceFileIdMap.add("1", new MethodId(MODULE,NAMESPACE,CLASS,"method1"));
	    sourceFileNamesTable.add("1", "myname");
		
		UnitTestingResults results=testResults.getTestingResults();
		results.newEntry().setClassName(FULLCLASSNAME)
			.setNamespaceNameFromClassName(FULLCLASSNAME)
			.setModuleFromCodeBase(MODULE)
			.setTestName("bogus").addToParent();

	   ProjectUnitTestResults projectUnitTestResults = parse();
		assertNotNull("parse should always return valid object",projectUnitTestResults);
		assertEquals("No results expected",0,projectUnitTestResults.values().size());
	}

    private ProjectUnitTestResults parse() {
        testResultsBuilder.parseCoverage(null);
            testResultsBuilder.parseTestResults(null);
        	ProjectUnitTestResults projectUnitTestResults=testResultsBuilder.getTestResults();
        return projectUnitTestResults;
    }
	
}
