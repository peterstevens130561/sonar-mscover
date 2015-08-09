package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;
import java.util.List;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugings.dotnet.resharper.issuesparser.InspectCodeResultsParser;
import com.stevpet.sonar.plugings.dotnet.resharper.saver.InspectCodeIssuesSaver;

public class DefaultReSharperWorkflow implements ResharperWorkflow {

    private InspectCodeResultsParser inspectCodeResultsParser;
    private InspectCodeIssuesSaver inspectCodeIssuesSaver;
    private InspectCodeRunner inspectCodeRunner;
    private List<InspectCodeIssue> issues;
    
    public DefaultReSharperWorkflow (InspectCodeResultsParser inspectCodeResultsParser,InspectCodeIssuesSaver inspectCodeIssuesSaver, InspectCodeRunner inspectCodeRunner) {
        this.inspectCodeResultsParser = inspectCodeResultsParser;
        this.inspectCodeIssuesSaver= inspectCodeIssuesSaver;
        this.inspectCodeRunner = inspectCodeRunner;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugings.dotnet.resharper.ResharperWorkflow#execute()
     */
    @Override
    public void execute() {
        File reportFile = inspectCodeRunner.inspectCode();
        issues = inspectCodeResultsParser.parse(reportFile);
        inspectCodeIssuesSaver.saveIssues(issues);
    }

    @Override
    public void executeModule(Project module) {
        File reportFile = inspectCodeRunner.inspectCode();
        issues = inspectCodeResultsParser.parse(reportFile);
        inspectCodeIssuesSaver.saveModuleIssues(issues,module);       
    }

}
