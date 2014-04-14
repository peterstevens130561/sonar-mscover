package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;

import java.io.File;

import org.junit.Test;
import org.junit.Assert;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;

public class ResultsParserTest {
    @Test
    public void parser_GetCounters_ShouldMatch() {
        ParserSubject parserSubject = new ResultsParserSubject();
        File file = TestUtils.getResource("results.trx");
        ResultsModel results = new ResultsModel();
        ResultsObserver resultsObserver = new ResultsObserver();
        resultsObserver.setRegistry(results);
        parserSubject.registerObserver(resultsObserver);
        parserSubject.parseFile(file);   
        Assert.assertEquals(120, results.getExecutedTests());
        Assert.assertEquals(20,results.getFailedTests());
        Assert.assertEquals(100,results.getPassedTests());
    }

    @Test
    public void parser_GetResults_ShouldMatch() {
        ParserSubject parserSubject = new ResultsParserSubject();
        File file = TestUtils.getResource("results.trx");
        UnitTestResultRegistry results = new UnitTestResultRegistry();
        UnitTestResultObserver resultsObserver = new UnitTestResultObserver();
        resultsObserver.setRegistry(results);
        parserSubject.registerObserver(resultsObserver);
        parserSubject.parseFile(file);
        Assert.assertEquals(186, results.size());
    }

    @Test
    public void checkModuleName_ShouldBeLastPart() {
        UnitTestResultModel result = new UnitTestResultModel() ;
        String codeBase="c:\\Development\\Jewel.Release.Oahu.StructMod\\JewelEarth\\Core\\joaGeometries\\joaGeometriesUnitTest\\Debug\\joaGeometriesUnitTests.dll";
        String expected = "joaGeometriesUnitTests.dll";
        result.setModuleFromCodeBase(codeBase);
        Assert.assertEquals(expected, result.getModuleName());
    }
    
    @Test
    public void checkNameSpace_ShouldBeFirstPart() {
        UnitTestResultModel result = new UnitTestResultModel() ;
        String className="joaGeometriesUnitTest.joaMeshTriangleTest";
        String expected = "joaGeometriesUnitTest";
        result.setNamespaceNameFromClassName(className);
        Assert.assertEquals(expected, result.getNamespaceName());
    }
    
    @Test
    public void checkNameSpace_ShouldBeEmpty() {
        UnitTestResultModel result = new UnitTestResultModel() ;
        String className="joaMeshTriangleTest";
        String expected = "";
        result.setNamespaceNameFromClassName(className);
        Assert.assertEquals(expected, result.getNamespaceName());
    }
    @Test
    public void parser_GetTest_ShouldMatch() {
        ParserSubject parserSubject = new ResultsParserSubject();
        File file = TestUtils.getResource("results.trx");
        UnitTestResultRegistry results = new UnitTestResultRegistry();
        UnitTestResultObserver resultsObserver = new UnitTestResultObserver();
        resultsObserver.setRegistry(results);
        parserSubject.registerObserver(resultsObserver);
        
        UnitTestObserver unitTestObserver = new UnitTestObserver() ;
        unitTestObserver.setRegistry(results);
        parserSubject.registerObserver(unitTestObserver);
        
        parserSubject.parseFile(file); 
        Assert.assertEquals(186, results.size());
        
        UnitTestResultModel model = results.getById("6debf182-8726-2cb3-6825-ea98526b8f76");
        Assert.assertNotNull(model);
        Assert.assertEquals("joaGeometriesUnitTest.joaQuadtreeTest", model.getClassName());
        Assert.assertEquals("c:\\Development\\Jewel.Release.Oahu.StructMod\\JewelEarth\\Core\\joaGeometries\\joaGeometriesUnitTest\\Debug\\joaGeometriesUnitTests.dll",model.getCodeBase());
    }

}
