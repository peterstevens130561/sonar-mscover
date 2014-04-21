package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;

public class ResultsParserSubject extends  ParserSubject {


    @Override
    public String[] getHierarchy() {
        String[] hierarchy = { "TestResults","TestSettings","Execution",
                "Results","TestDefinitions","UnitTest","UnitTestResult",
                "TestEntries","TestLists","ResultSummary",
                "UriAttachments","UriAttachment",
                "CollectorDataEntries","Collector","Output","ErrorInfo","Message"};
        return hierarchy;
    }
}