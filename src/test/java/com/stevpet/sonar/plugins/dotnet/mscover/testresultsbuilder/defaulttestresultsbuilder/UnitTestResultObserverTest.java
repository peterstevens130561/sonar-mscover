package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestingResults;

public class UnitTestResultObserverTest {

    private UnitTestResultObserver observer = new UnitTestResultObserver();
    
    @Test
    public void someMilliSeconds() {
        UnitTestingResults unitTestingResults = new UnitTestingResults();
        observer.setRegistry(unitTestingResults);
        long duration = 1234567 ;
        observer.testId("1");
        observer.duration("00:00:99.12346795");
        UnitTestMethodResult result = unitTestingResults.getById("1");
        assertEquals("99123.466",result.getDuration());
    }
    
    @Test
    public void oneMilliSeconds() {
        UnitTestingResults unitTestingResults = new UnitTestingResults();
        observer.setRegistry(unitTestingResults);
        long duration = 1234567 ;
        observer.testId("1");
        observer.duration("00:00:00.001230");
        UnitTestMethodResult result = unitTestingResults.getById("1");
        assertEquals("1.230",result.getDuration());
    }
}
