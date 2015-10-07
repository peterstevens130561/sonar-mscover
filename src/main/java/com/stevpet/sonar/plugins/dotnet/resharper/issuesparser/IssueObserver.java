package com.stevpet.sonar.plugins.dotnet.resharper.issuesparser;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;

public class IssueObserver extends BaseParserObserver {
    private InspectCodeIssue issue;
    private List<InspectCodeIssue> issues;
    public IssueObserver(List<InspectCodeIssue> inspectCodeIssues) {
        this.issues = inspectCodeIssues;
        setPattern("Issues/Project/Issue");
    }   
    
    /**
     * mandatory typeId
     * @param value
     */
    @AttributeMatcher(elementName="Issue", attributeName = "TypeId")
    public void typeIdMatcher(String value) {
        issue=new InspectCodeIssue();
        issue.setTypeId(value);
        issue.setLine("1"); // in case there is no line attribute
    }
    
    /**
     * mandatory file attribute
     * @param value
     */
    @AttributeMatcher(elementName="Issue", attributeName = "File")
    public void fileMatcher(String value) {
        issue.setRelativePath(value);
    }
    /**
     * Optional line attribute
     * @param value
     */
    @AttributeMatcher(elementName="Issue", attributeName="Line")
    public void lineMatcher(String value) {
        issue.setLine(value);
    }
    
    /**
     * Optional msaage
     * @param value
     */
    @AttributeMatcher(elementName="Issue", attributeName="Message") 
    public void messageMachter(String value) {
        issue.setMessage(value);
        issues.add(issue);
    }
}