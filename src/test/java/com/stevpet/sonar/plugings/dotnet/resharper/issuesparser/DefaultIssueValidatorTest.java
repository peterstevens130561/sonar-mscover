package com.stevpet.sonar.plugings.dotnet.resharper.issuesparser;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugings.dotnet.resharper.InspectCodeIssue;

public class DefaultIssueValidatorTest {

    IssueValidator issueValidator;
    private List<InspectCodeIssue> issues;
    @Before
    public void before() {
        issueValidator = new DefaultIssueValidator();
        issues = new ArrayList<>();
    }
    
    @Test
    public void normalIssue_shouldPass() {
        createIssue("RedundantNameQualifier","Qualifier is redundant");

        try {
            issueValidator.validate(issues);
        } catch (Exception e) {
            fail("should not get any exception for a normal issue");
        }
 
    }
    
    @Test
    public void goodNamingIssue_shouldPass() {
        createIssue("InconsistentNaming","Name 'm_isTSR' does not match rule 'Instance fields (private)'. Suggested name is 'm_isTsr'.");

        try {
            issueValidator.validate(issues);
        } catch (Exception e) {
            fail("should not get any exception for a normal issue");
        }
        
    }
    
    @Test
    public void wrongNamingIssue_shouldFail() {
        createIssue("InconsistentNaming","Name 'm_isTSR' does not match rule 'Instance fields (private)'. Suggested name is '_misTsr'.");

        try {
            issueValidator.validate(issues);
        } catch (IssueValidationException e) {
            return;
        }
        fail("should have gotten issueValidationException");       
    }
    
    @Test
    public void wrongCSharpErrore_shouldFail() {
        createIssue("CSharpErrors","Completely bogus");

        try {
            issueValidator.validate(issues);
        } catch (IssueValidationException e) {
            return;
        }
        fail("should have gotten issueValidationException");       
    }
    private void createIssue(String typeId, String message) {
        InspectCodeIssue normalIssue = new InspectCodeIssue();
        normalIssue.setTypeId(typeId);
        normalIssue.setMessage(message);
        normalIssue.setRelativePath("relative");
        issues.add(normalIssue);        
    }
}
