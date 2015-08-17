package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugings.dotnet.resharper.issuesparser.InspectCodeResultsParser;
import com.stevpet.sonar.plugings.dotnet.resharper.issuesparser.IssueValidationException;
import com.stevpet.sonar.plugings.dotnet.resharper.issuesparser.IssueValidator;
import com.stevpet.sonar.plugings.dotnet.resharper.saver.InspectCodeIssuesSaver;

public class DefaultReSharperWorkflow implements ResharperWorkflow {
    private Logger LOG = LoggerFactory.getLogger(DefaultReSharperWorkflow.class);
    private InspectCodeResultsParser inspectCodeResultsParser;
    private InspectCodeIssuesSaver inspectCodeIssuesSaver;
    private InspectCodeRunner inspectCodeRunner;
    private List<InspectCodeIssue> issues;
    private IssueValidator issueValidator;

    public DefaultReSharperWorkflow(InspectCodeResultsParser inspectCodeResultsParser,
            InspectCodeIssuesSaver inspectCodeIssuesSaver, InspectCodeRunner inspectCodeRunner, IssueValidator issueValidator) {
        this.inspectCodeResultsParser = inspectCodeResultsParser;
        this.inspectCodeIssuesSaver = inspectCodeIssuesSaver;
        this.inspectCodeRunner = inspectCodeRunner;
        this.issueValidator = issueValidator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugings.dotnet.resharper.ResharperWorkflow#execute()
     */
    @Override
    public void execute() {
        for (int retryCnt = 0; retryCnt < 3; retryCnt++) {
            File reportFile = inspectCodeRunner.inspectCode();
            issues = inspectCodeResultsParser.parse(reportFile);
            issueValidator.validate(issues);
            if (!issueValidator.validationFailed()) {
                break;
            }
            IssueValidationException exception = issueValidator.getException();
            LOG.warn("IssueValidation failed {}", exception.getMessage());
            inspectCodeRunner.dropCache();
        }
        if(issueValidator.validationFailed()) {
            throw issueValidator.getException();
        }
        inspectCodeIssuesSaver.saveIssues(issues);
    }

    @Override
    public void executeModule(Project module) {
        File reportFile = inspectCodeRunner.inspectCode();
        issues = inspectCodeResultsParser.parse(reportFile);
        issueValidator.validate(issues);
        inspectCodeIssuesSaver.saveModuleIssues(issues, module);
    }

}
