package com.stevpet.sonar.plugins.dotnet.resharper.issuesparser;

import java.util.List;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;

public interface IssueValidator extends BatchExtension{

    /**
     * Given a list of issues, validate each of them.
     * @param issues - list of resharper issues
     * @throws IssueValidationException in case of an invalid issue
     */
    void validate(List<InspectCodeIssue> issues);

    boolean validationFailed();

    IssueValidationException getException();

}