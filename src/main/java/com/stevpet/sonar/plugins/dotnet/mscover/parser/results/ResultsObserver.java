package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;


import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;

public class ResultsObserver extends BaseParserObserver {


    public ResultsObserver() {
        setPattern("ResultSummary/Counters");
    }
    
    private ResultsModel data;

    public void setRegistry(ResultsModel data) {
        this.data = data;
    }
    
   
    @AttributeMatcher(attributeName = "executed", elementName = "Counters")
    public void executedMatcher(String attributeValue) {
        data.setExecutedTests(Integer.parseInt(attributeValue));
    }
    
    @AttributeMatcher(attributeName="passed",elementName="Counters")
    public void passedMatcher(String attributeValue) {
        data.setPassedTests(Integer.parseInt(attributeValue));
    }

    @AttributeMatcher(attributeName="failed",elementName="Counters")
    public void failedMatcher(String attributeValue) {
        data.setFailedTests(Integer.parseInt(attributeValue));
    }
    
    @AttributeMatcher(attributeName="error",elementName="Counters")
    public void erroredMatcher(String attributeValue) {
        data.setErroredTests(Integer.parseInt(attributeValue));
    }
}
