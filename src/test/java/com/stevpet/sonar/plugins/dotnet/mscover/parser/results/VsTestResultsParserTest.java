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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;

import java.io.File;

import org.junit.Test;
import org.junit.Assert;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.TestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.VsTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.ResultsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.UnitTestDefinitionObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.UnitTestResultObserver;

public class VsTestResultsParserTest {
    @Test
    public void parser_GetCounters_ShouldMatch() {
        XmlParser xmlParser = createNewParser();
        File file = TestUtils.getResource("results.trx");
        TestResults results = new TestResults();
        ResultsObserver resultsObserver = new ResultsObserver();
        resultsObserver.setRegistry(results);
        xmlParser.registerObserver(resultsObserver);
        xmlParser.parseFile(file);   
        Assert.assertEquals(120, results.getExecutedTests());
        Assert.assertEquals(20,results.getFailedTests());
        Assert.assertEquals(100,results.getPassedTests());
    }

    @Test
    public void parser_GetResults_ShouldMatch() {
        XmlParser xmlParser = createNewParser();
        File file = TestUtils.getResource("results.trx");
        VsTestResults results = new VsTestResults();
        UnitTestResultObserver resultsObserver = new UnitTestResultObserver();
        resultsObserver.setRegistry(results);
        xmlParser.registerObserver(resultsObserver);
        xmlParser.parseFile(file);
        Assert.assertEquals(186, results.size());
    }

    private XmlParser createNewParser() {
        return new DefaultXmlParser();
    }

    
    @Test
    public void parser_GetResultsWithError_ShouldMatch() {
        XmlParser xmlParser = createNewParser();
        File file = TestUtils.getResource("ResultsWithError.trx");
        VsTestResults results = new VsTestResults();
        UnitTestResultObserver resultsObserver = new UnitTestResultObserver();
        resultsObserver.setRegistry(results);
        xmlParser.registerObserver(resultsObserver);
        xmlParser.parseFile(file);
        Assert.assertEquals(4, results.size());
        UnitTestMethodResult result=results.getById("2b700139-4cbd-7db0-4b54-6f23eab71b6b");
        Assert.assertTrue(result.getMessage().startsWith("Test method joaFrameworkUnitTests.joaSolutionTest.NewSolutionTest"));
        String stackTrace=result.getStackTrace();
        Assert.assertNotNull(stackTrace);
        Assert.assertTrue(stackTrace.startsWith("at joaFramework.SolutionBase..ctor()"));
    }
    @Test
    public void checkModuleName_ShouldBeLastPart() {
        UnitTestMethodResult result = newUnitTestMethodResult() ;
        String codeBase="c:\\Development\\Jewel.Release.Oahu.StructMod\\JewelEarth\\Core\\joaGeometries\\joaGeometriesUnitTest\\Debug\\joaGeometriesUnitTests.dll";
        String expected = "joaGeometriesUnitTests.dll";
        result.setModuleFromCodeBase(codeBase);
        Assert.assertEquals(expected, result.getModuleName());
    }

    private UnitTestMethodResult newUnitTestMethodResult() {
        return new UnitTestMethodResult("SOMEID");
    }
    
    @Test
    public void checkNameSpace_ShouldBeFirstPart() {
        UnitTestMethodResult result = newUnitTestMethodResult() ;
        String className="joaGeometriesUnitTest.joaMeshTriangleTest";
        String expected = "joaGeometriesUnitTest";
        result.setNamespaceNameFromClassName(className);
        Assert.assertEquals(expected, result.getNamespaceName());
    }
    
    @Test
    public void checkNameSpace_ShouldBeEmpty() {
        UnitTestMethodResult result = newUnitTestMethodResult() ;
        String className="joaMeshTriangleTest";
        String expected = "";
        result.setNamespaceNameFromClassName(className);
        Assert.assertEquals(expected, result.getNamespaceName());
    }
    @Test
    public void parser_GetTest_ShouldMatch() {
        XmlParser xmlParser = createNewParser();
        File file = TestUtils.getResource("results.trx");
        VsTestResults results = new VsTestResults();
        UnitTestResultObserver resultsObserver = new UnitTestResultObserver();
        resultsObserver.setRegistry(results);
        xmlParser.registerObserver(resultsObserver);
        
        UnitTestDefinitionObserver unitTestDefinitionObserver = new UnitTestDefinitionObserver() ;
        unitTestDefinitionObserver.setRegistry(results);
        xmlParser.registerObserver(unitTestDefinitionObserver);
        
        xmlParser.parseFile(file); 
        Assert.assertEquals(186, results.size());
        
        UnitTestMethodResult model = results.getById("6debf182-8726-2cb3-6825-ea98526b8f76");
        Assert.assertNotNull(model);
        Assert.assertEquals("TestQuadTreeGetNodeDataInRangeMockSGrid",model.getTestName());
        Assert.assertEquals("joaQuadtreeTest", model.getClassName());
        Assert.assertEquals("joaGeometriesUnitTest", model.getNamespaceName());
        Assert.assertEquals("c:\\Development\\Jewel.Release.Oahu.StructMod\\JewelEarth\\Core\\joaGeometries\\joaGeometriesUnitTest\\Debug\\joaGeometriesUnitTests.dll",model.getCodeBase());
    }

}
