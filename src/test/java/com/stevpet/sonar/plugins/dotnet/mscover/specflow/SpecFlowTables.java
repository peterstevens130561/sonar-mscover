package com.stevpet.sonar.plugins.dotnet.mscover.specflow;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sonar.test.TestUtils;

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
        File testResultsFile = TestUtils.getResource("SpecFlow/SpecFlow.trx");
        File testCoverageFile = TestUtils.getResource("SpecFlow/specflow.coveragexml");
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
        assertEquals("number of tests",2,unitTestRegistry.getResults().size());
        assertEquals("files",227,testCoverageRegistry.getSourceFileNameTable().size());
                
        UnitTestFilesResultRegistry filesResultRegistry = new UnitTestFilesResultRegistry();

        UnitTestResultRegistry unitTestResults=unitTestRegistry.getResults();
        filesResultRegistry.mapResults(unitTestResults,methodToSourceFileIdMap);
        
        
        
    }
}
