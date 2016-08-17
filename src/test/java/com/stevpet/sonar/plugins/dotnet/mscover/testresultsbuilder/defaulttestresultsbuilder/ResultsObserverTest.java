package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.model.TestResults;


public class ResultsObserverTest {
    private final ResultsObserver observer = new ResultsObserver();
    private TestResults data;
    
    @Before
    public void before() {
        XmlParserSubject parser = new VsTestResultsParserSubject();

        File xmlFile = TestUtils.getResource("observers/ResultsObserver.xml");
        data= new TestResults();
        parser.registerObserver(observer);
        observer.setRegistry(data);
        parser.parseFile(xmlFile);
    }
    
    @Test
    public void checkExecuted() {
        assertEquals("executed",120,data.getExecutedTests());
    }
    
    @Test
    public void checkPassed() {
        assertEquals("passed",100,data.getPassedTests());
    }
    
    @Test
    public void checkFailed() {
        assertEquals("failed",20,data.getFailedTests());
    }
    
    @Test
    public void checkErrored() {
        assertEquals("error",5,data.getErroredTests());
    }
}
