package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsParser;


public class DefaultTestResultsParserTest {

    private final TestResultsParser parser = new DefaultTestResultsParser() ;
    private UnitTestRegistry registry;
    
    @Before
    public void before() {
        File file = TestUtils.getResource("TestResults/goldstandard.trx");
        assertNotNull("could not get file ",file);
        parser.parse(file);
        registry=parser.getUnitTestRegistry();
    }
    
    @Test
    public void resultsTest() {
        assertEquals("expect 1 result",1,registry.getTestingResults().size());
    }
    
    @Test
    public void testnameTest() {
        Collection<UnitTestMethodResult> results = registry.getTestingResults().values();
        UnitTestMethodResult result = results.iterator().next();
        assertEquals("WhenCalled_SetsCurrentJewelSuiteFolderOnView",result.getTestName());
        
    }
    
    
}