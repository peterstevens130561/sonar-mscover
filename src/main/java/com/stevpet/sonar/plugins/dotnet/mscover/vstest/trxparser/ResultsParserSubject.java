package com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.ParserSubject;

public class ResultsParserSubject extends  ParserSubject {


    @Override
    public String[] getHierarchy() {
        String[] hierarchy = { "TestResults","TestSettings","Execution",
                "Results","TestDefinitions","UnitTest","UnitTestResult",
                "TestEntries","TestLists","ResultSummary","RunInfos","RunInfo",
                "UriAttachments","UriAttachment",
                "CollectorDataEntries","Collector","Output","ErrorInfo"};
        return hierarchy;
    }
}
