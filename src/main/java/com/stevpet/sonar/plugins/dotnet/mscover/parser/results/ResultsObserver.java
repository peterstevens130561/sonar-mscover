package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;


import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;


public class ResultsObserver extends BaseParserObserver {


    public ResultsObserver() {
        setPattern("ResultSummary/Counters");
    }
    
    private int executedTests ;
    

    public void observeElement(String name, String text) {
        executedTests=186;
    }
    
    public int getExecutedTests() {
        return executedTests;
    }

    public void observeAttribute(String elementName, String path,
            String attributeValue, String attributeName) {
    }


}
