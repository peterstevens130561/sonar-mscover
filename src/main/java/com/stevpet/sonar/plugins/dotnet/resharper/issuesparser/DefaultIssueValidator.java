package com.stevpet.sonar.plugins.dotnet.resharper.issuesparser;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;

public class DefaultIssueValidator implements IssueValidator {
    private IssueValidationException exception;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueValidator
     * #validate(java.util.List)
     */
    @Override
    public void validate(List<InspectCodeIssue> issues) {
        exception=null;
        for (InspectCodeIssue issue : issues) {
            validateIssue(issue);
            if(exception!=null) {
                break;
            }
        }
    }

    private void validateIssue(InspectCodeIssue issue) {
        String message = issue.getMessage();
        if (StringUtils.isEmpty(message)) {
            return;
        }
        String typeId = issue.getTypeId();
        if (StringUtils.isEmpty(typeId)) {
            return;
        }

        if (typeId.equalsIgnoreCase("InconsistentNaming")
                && message.contains("does not match rule 'Instance fields (private)'. Suggested name is '_m")) {
            exception=new IssueValidationException(issue);
        }
    }

    @Override
    public boolean validationFailed() {
        return exception!=null;
    }

    @Override
    public IssueValidationException getException() {
        return exception;
    }
}
