package com.stevpet.sonar.plugings.dotnet.resharper.issuesparser;

import com.stevpet.sonar.plugings.dotnet.resharper.InspectCodeIssue;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class IssueValidationException extends MsCoverException {

    private static final long serialVersionUID = 1L;

    public IssueValidationException(InspectCodeIssue issue) {
        super("Failed on issue " + issue.getTypeId() + " " + issue.getMessage() + " " + issue.getRelativePath());
    }
}
