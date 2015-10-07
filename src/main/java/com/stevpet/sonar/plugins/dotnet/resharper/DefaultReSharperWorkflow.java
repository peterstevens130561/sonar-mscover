package com.stevpet.sonar.plugins.dotnet.resharper;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.InspectCodeResultsParser;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueValidationException;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueValidator;
import com.stevpet.sonar.plugins.dotnet.resharper.saver.InspectCodeIssuesSaver;

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
     * com.stevpet.sonar.plugins.dotnet.resharper.ResharperWorkflow#execute()
     */
    @Deprecated
    @Override
    public void execute() {
        inspectSolution();
        inspectCodeIssuesSaver.saveIssues(issues);
    }

    public void inspectSolution() {
        for (int retryCnt = 0; retryCnt < 1; retryCnt++) {
            File reportFile = inspectCodeRunner.inspectCode();
            issues = inspectCodeResultsParser.parse(reportFile);
            issueValidator.validate(issues);
            if (!issueValidator.validationFailed()) {
                break;
            }
            IssueValidationException exception = issueValidator.getException();
            LOG.warn("IssueValidation failed {} on try {}", exception.getMessage(),retryCnt+1);
            inspectCodeRunner.dropCache();
        }
        if(issueValidator.validationFailed()) {
            throw issueValidator.getException();
        }
    }

    @Override
    public void executeModule(Project module) {
        inspectSolution();
        inspectCodeIssuesSaver.saveModuleIssues(issues, module);
    }

}
