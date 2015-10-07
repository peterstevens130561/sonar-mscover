package com.stevpet.sonar.plugins.dotnet.resharper.issuesparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;

public class DefaultInspectCodeResultsParser implements
        InspectCodeResultsParser {

    @Override
    public List<InspectCodeIssue> parse(File file) {
        InspectCodeParserSubject parser = new InspectCodeParserSubject();
        List<InspectCodeIssue> issues = new ArrayList<InspectCodeIssue>();
        IssueObserver issueObserver = new IssueObserver(issues);
        parser.registerObserver(issueObserver);
        parser.parseFile(file);
        return issues;
    }

}
