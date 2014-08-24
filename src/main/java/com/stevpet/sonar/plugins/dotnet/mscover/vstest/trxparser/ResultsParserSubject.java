package com.stevpet.sonar.plugins.dotnet.mscover.vstest.trxparser;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;

public class ResultsParserSubject extends  XmlParserSubject {


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
