package com.stevpet.sonar.plugins.dotnet.resharper.issuesparser;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;
import com.stevpet.sonar.plugins.dotnet.resharper.exceptions.InspectCodeException;

public class IssueValidationException extends InspectCodeException {

    private static final long serialVersionUID = 1L;

    public IssueValidationException(InspectCodeIssue issue) {
        super("Failed on issue " + issue.getTypeId() + " " + issue.getMessage() + " " + issue.getRelativePath());
    }
}
