package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry.ForEachUnitTestFile;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestSourceFileNamesToSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestMethodToSourceFileIdMapObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.ResultsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.ResultsParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.UnitTestObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser.UnitTestResultObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MsCoverCanNotFindSourceFileForMethodException;

public class IntegrationTest {
    MethodToSourceFileIdMap methodToSourceFileIdMap;
    SourceFileNameTable sourceFileNamesRegistry;
    ResultsModel testResultsModel;
    
    @Before() 
    public void before() {
        methodToSourceFileIdMap = new MethodToSourceFileIdMap() ;
        sourceFileNamesRegistry = new SourceFileNameTable();
        testResultsModel = new ResultsModel() ;
    }
    @Test 
    public void CompleteTest() {

        UnitTestFilesResultRegistry filesResultRegistry = new UnitTestFilesResultRegistry();
        UnitTestResultRegistry unitTestRegistry = new UnitTestResultRegistry();
        
        ResultsModel resultsModel = parseResults(unitTestRegistry,"Mileage/results.trx");
        
        Assert.assertEquals(4,resultsModel.getExecutedTests());
        Assert.assertEquals(1,resultsModel.getFailedTests());
        Assert.assertEquals(0,(int)resultsModel.getErroredTests());
      
        parseCoverageToGetMethodToSourceFileIdMap("Mileage/coverage.xml");

        filesResultRegistry.mapResults(unitTestRegistry,methodToSourceFileIdMap);
        //Assert
        filesResultRegistry.forEachUnitTestFile(new checkUnitTest());
        
    }

    @Test(expected=MsCoverCanNotFindSourceFileForMethodException.class)
    public void CompleteTest_DuplicatedTest_ShouldThrowException() {

        UnitTestFilesResultRegistry filesResultRegistry = new UnitTestFilesResultRegistry();
        UnitTestResultRegistry unitTestRegistry = new UnitTestResultRegistry();
        
        ResultsModel resultsModel = parseResults(unitTestRegistry,"Mileage/results_duplicate.trx");
        
        Assert.assertEquals(4,resultsModel.getExecutedTests());
        Assert.assertEquals(1,resultsModel.getFailedTests());
        Assert.assertEquals(0,(int)resultsModel.getErroredTests());
      
        parseCoverageToGetMethodToSourceFileIdMap("Mileage/coverage.xml");

        filesResultRegistry.mapResults(unitTestRegistry,methodToSourceFileIdMap);
        //Assert
        filesResultRegistry.forEachUnitTestFile(new checkUnitTest());
        
    }
    private void parseCoverageToGetMethodToSourceFileIdMap(String path) {
        VsTestMethodToSourceFileIdMapObserver methodObserver = new VsTestMethodToSourceFileIdMapObserver();
        methodObserver.setRegistry(methodToSourceFileIdMap);

        CoverageParserSubject coverageParser = new CoverageParserSubject();
        coverageParser.registerObserver(methodObserver);
        

        VsTestSourceFileNamesToSourceFileNamesObserver sourceFileNamesObserver = new VsTestSourceFileNamesToSourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        coverageParser.registerObserver(sourceFileNamesObserver);

        File coverageFile = TestUtils.getResource(path);
        coverageParser.parseFile(coverageFile);
    }

    private ResultsModel parseResults(UnitTestResultRegistry unitTestRegistry,String path) {
        ResultsObserver resultsObserver = new ResultsObserver();

        resultsObserver.setRegistry(testResultsModel);
       
        XmlParserSubject resultsParser = new ResultsParserSubject();    
        UnitTestResultObserver unitTestResultObserver = new UnitTestResultObserver();

        unitTestResultObserver.setRegistry(unitTestRegistry);
        resultsParser.registerObserver(unitTestResultObserver);
        
        UnitTestObserver unitTestObserver = new UnitTestObserver();
        unitTestObserver.setRegistry(unitTestRegistry);
        resultsParser.registerObserver(unitTestObserver);
        resultsParser.registerObserver(resultsObserver);
        
        File file = TestUtils.getResource(path);
        if(!file.exists()) {
            throw new SonarException("Can't open " + file.getAbsolutePath() );
        }
        // just the totals
        resultsParser.parseFile(file);
        return testResultsModel;
    }
    
    private class checkUnitTest implements ForEachUnitTestFile {

        public void execute(String fileId, UnitTestFileResultModel unitTest) {
            Assert.assertEquals("1", fileId);
        }
        
    }
}
