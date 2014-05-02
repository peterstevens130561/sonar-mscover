package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;

/*
 *   <UnitTestResult executionId="a3a9f8e0-1cbf-41c5-ae89-a579d2e6ece5" 
 *   testId="865b2842-73dd-b3d0-17a4-284b86e8ce98" 
 *   testName="TestBoxNotIntersects" 
 *   computerName="RDSJ741TY1" 
 *   duration="00:00:00.0006819" 
 *   startTime="2014-04-10T20:59:56.5584722+02:00" 
 *   endTime="2014-04-10T20:59:56.5594723+02:00" 
 *   testType="13cdc9d9-ddb5-4fa4-a97d-d965ccfc6d4b" 
 *   outcome="Passed" 
 *   testListId="8c84fa94-04c1-424b-9868-57a2d4851a1d" 
 *   relativeResultsDirectory="a3a9f8e0-1cbf-41c5-ae89-a579d2e6ece5" />
 */
public class UnitTestResultObserver extends BaseParserObserver {

    public UnitTestResultObserver() {
        setPattern("(Results/UnitTestResult)|(.*/Message)|(.*/StackTrace)");
    }
    
    private UnitTestResultRegistry registry;
    private UnitTestResultModel unitTestResult;
    public void setRegistry(UnitTestResultRegistry registry) {
        this.registry = registry;
    }
 
    @AttributeMatcher(attributeName = "testId", elementName = "UnitTestResult")
    public void testId(String value) {
        unitTestResult = new UnitTestResultModel();
        unitTestResult.setTestId(value);
    }
    @AttributeMatcher(attributeName = "testName", elementName = "UnitTestResult")
    public void testName(String value) {
        unitTestResult.setTestName(value);
    }
 
    @AttributeMatcher(attributeName = "outcome", elementName = "UnitTestResult") 
    public void outcome(String value) {
        unitTestResult.setOutcome(value);
    }
    
    @AttributeMatcher(attributeName= "duration", elementName = "UnitTestResult")
    public void duration(String value) {
        unitTestResult.setDuration(value);
    }
    
    @AttributeMatcher(attributeName= "relativeResultsDirectory",elementName = "UnitTestResult")
    public void relativeResultsDirectory(String value) {
        unitTestResult.setRelativeResultsDirectory(value);
        registry.add(unitTestResult);
    }
    
    @ElementMatcher(elementName="Message")
    public void message(String value) {
        unitTestResult.setMessage(value);
    }
    
    @ElementMatcher(elementName="StackTrace")
    public void stackTrace(String value) {
        unitTestResult.setStackTrace(value);
    }
}
