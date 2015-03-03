package com.stevpet.sonar.plugins.dotnet.mscover.specflow;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ConcreteVsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.ConcreteVsTestFactory;

public class SpecFlowTables {

    @Test
    public void testSpecFlowTables() {
        //given
        File testResultsFile = TestUtils.getResource("SpecFlow/2_SpecFlow.trx");
        File testCoverageFile = TestUtils.getResource("SpecFlow/2_specflow.coveragexml");
        //when
        assertTrue("results file does not exist",testResultsFile.exists());
        assertTrue("coverage file does not exist",testCoverageFile.exists());
        UnitTestRegistry unitTestRegistry = new UnitTestRegistry();
        XmlParserSubject resultsParser = new ConcreteVsTestFactory().createUnitTestResultsParser(unitTestRegistry);
        resultsParser.parseFile(testResultsFile);
        
        VsTestParserFactory parserFactory = new ConcreteVsTestParserFactory();
        VsTestCoverageRegistry testCoverageRegistry = new VsTestCoverageRegistry("bogusdir");
        List<String> artifactNames= new ArrayList<String>();
        XmlParserSubject parserSubject = parserFactory.createCoverageParser(testCoverageRegistry,artifactNames);
        parserSubject.parseFile(testCoverageFile);
        
        MethodToSourceFileIdMap methodToSourceFileIdMap =testCoverageRegistry.getMethodToSourceFileIdMap();
        SourceFileNameTable sourceFileNamesRegistry=testCoverageRegistry.getSourceFileNameTable();
        parserSubject=parserFactory.createFileNamesParser(methodToSourceFileIdMap, sourceFileNamesRegistry);
        parserSubject.parseFile(testCoverageFile);
        assertEquals("number of tests",609,unitTestRegistry.getResults().size());
        assertEquals("files",239,testCoverageRegistry.getSourceFileNameTable().size());
                
        UnitTestFilesResultRegistry filesResultRegistry = new UnitTestFilesResultRegistry();

        UnitTestResultRegistry unitTestResults=unitTestRegistry.getResults();
        filesResultRegistry.mapResults(unitTestResults,methodToSourceFileIdMap);   
        
        MethodId methodId = new MethodId();
        methodId.setNamespaceName("BHI.Framework.Geometries.UnitTest.Features.Vector2D");
        methodId.setModuleName("bhi.framework.geometries.unittest.dll");
        methodId.setClassName("Vector2DUnaryOperatorsFeature");
        
        String[] tests = {"NegatingA2DVector_00","NegatingA2DVector_12","NegatingA2DVector_3_5","NormalizingA2DVector_00"};
        for(String test: tests) {
            methodId.setMethodName(test);
            String fileId=methodToSourceFileIdMap.getLongestContainedMethod(methodId);
            assertNotNull("test " + test,fileId);
        }
       
    }
    
}
