package com.stevpet.sonar.plugings.dotnet.resharper;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;

public class IssueObserver extends BaseParserObserver {
    private InspectCodeIssue issue;
    private List<InspectCodeIssue> issues;
    public IssueObserver(List<InspectCodeIssue> inspectCodeIssues) {
        this.issues = inspectCodeIssues;
        setPattern("Issues/Project/Issue");
    }   
    
    @AttributeMatcher(elementName="Issue", attributeName = "TypeId")
    public void typeIdMatcher(String value) {
        issue=new InspectCodeIssue();
        issue.setTypeId(value);
    }
    
    @AttributeMatcher(elementName="Issue", attributeName = "File")
    public void fileMatcher(String value) {
        issue.setRelativePath(value);
    }
    
    @AttributeMatcher(elementName="Issue", attributeName="Line")
    public void lineMatcher(String value) {
        issue.setLine(value);
    }
    
    @AttributeMatcher(elementName="Issue", attributeName="Message") 
    public void messageMachter(String value) {
        issue.setMessage(value);
        issues.add(issue);
    }
}