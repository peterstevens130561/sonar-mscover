package com.stevpet.sonar.plugings.dotnet.resharper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.File;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.MessageException;

import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultInspectCodeIssuesSaver implements InspectCodeIssuesSaver {

    private Logger Log = LoggerFactory.getLogger(DefaultInspectCodeIssuesSaver.class);
    private ResourcePerspectives perspectives;
    public DefaultInspectCodeIssuesSaver(ResourcePerspectives resourcePerspectives, ResourceResolver resourceResolver) {
        this.perspectives = resourcePerspectives;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugings.dotnet.resharper.InspectCodeIssuesSaver#saveIssues
     * (java.util.List)
     */
    @Override
    public void saveIssues(List<InspectCodeIssue> issues) {
        for (InspectCodeIssue issue : issues) {
            saveIssue(issue);
        }
    }

    private void saveIssue(InspectCodeIssue inspectCodeIssue) {
        String relativePath = inspectCodeIssue.getRelativePath();
        File myResource = File.create(relativePath);
        
        if (myResource == null) {
            Log.debug("could not resolve " + inspectCodeIssue.getRelativePath());
            return;
        }
        Issuable issuable = perspectives.as(Issuable.class, myResource);
        if (issuable == null) {
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
            Log.warn(e.getMessage());
        }
    }
}
