package com.stevpet.sonar.plugins.dotnet.resharper.saver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.MessageException;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultInspectCodeIssuesSaver implements InspectCodeIssuesSaver {

    private Logger Log = LoggerFactory.getLogger(DefaultInspectCodeIssuesSaver.class);
    private ResourcePerspectives resourcePerspectives;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    public DefaultInspectCodeIssuesSaver(ResourcePerspectives resourcePerspectives, MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.resourcePerspectives = resourcePerspectives;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }

    @Override
    public void saveIssues(List<InspectCodeIssue> issues) {
        for (InspectCodeIssue issue : issues) {
            saveIssue(issue);
        }
    }

    @Override
    public void saveModuleIssues(List<InspectCodeIssue> issues, Project module) {
        if(microsoftWindowsEnvironment.isUnitTestProject(module)) {
            return;
        }
        
        for (InspectCodeIssue issue : issues) {
            saveModuleIssue(issue,module);
        } 
    }
    
    private void saveModuleIssue(InspectCodeIssue inspectCodeIssue, Project module) {
        String relativeIssuePath = inspectCodeIssue.getRelativeUnixPath();
        String beginPath = module.getPath() + "/";
        if (!relativeIssuePath.startsWith(beginPath)) {
            return;
        }
        int offset = beginPath.length();
        String childPath = relativeIssuePath.substring(offset);
        saveIssuable(inspectCodeIssue, childPath);
    }

    private void saveIssue(InspectCodeIssue inspectCodeIssue) {
        String relativePath = inspectCodeIssue.getRelativePath();
        saveIssuable(inspectCodeIssue, relativePath);
    }

    private void saveIssuable(InspectCodeIssue inspectCodeIssue, String relativePath) {
        if(isIssueOfTestFile(inspectCodeIssue)) {
            Log.debug("ignoring test file {}",relativePath);
            return;
        }
        Issuable issuable = createIssuable(relativePath);
        if(issuable==null) {
            return;
        }
        int line = Integer.parseInt(inspectCodeIssue.getLine());
        RuleKey key = RuleKey.of("resharper-cs", inspectCodeIssue.getTypeId());
        String message = inspectCodeIssue.getMessage();
        Issue issue = issuable.newIssueBuilder()
                .ruleKey(key)
                .line(line)
                .message(message).build();
        try {
            issuable.addIssue(issue);
        } catch (MessageException e) {
            Log.warn("exception thrown during saving issue: ",e.getMessage());
        }
    }


    private Issuable createIssuable( String relativePath) {
        
        File myResource = File.create(relativePath);
        if (myResource == null) {
            Log.debug("could not resolve " + relativePath);
            return null;
        }
        Issuable issuable = resourcePerspectives.as(Issuable.class, myResource);
        if (issuable == null) {
            Log.debug("could not create issuable for " + relativePath);
            return null;
        }
        return issuable;
    }
    

    private boolean isIssueOfTestFile(InspectCodeIssue inspectCodeIssue) {
        String relativePath = inspectCodeIssue.getRelativePath();
        java.io.File sourceFile=new java.io.File(microsoftWindowsEnvironment.getCurrentSolution().getSolutionDir(),relativePath);
        List<java.io.File> files = microsoftWindowsEnvironment.getUnitTestSourceFiles();
        return files.contains(sourceFile);    
    }


}
