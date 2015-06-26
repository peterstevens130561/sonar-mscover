package com.stevpet.sonar.plugings.dotnet.resharper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.component.Perspectives;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.internal.DefaultIssue;
import org.sonar.api.resources.File;
import org.sonar.api.rule.RuleKey;

import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultInspectCodeIssuesSaver {

    private Logger Log = LoggerFactory.getLogger(DefaultInspectCodeIssuesSaver.class);
    private ResourcePerspectives perspectives;
    private ResourceResolver resourceResolver;

    public DefaultInspectCodeIssuesSaver(ResourcePerspectives resourcePerspectives,ResourceResolver resourceResolver) {
        this.perspectives = resourcePerspectives;
        this.resourceResolver = resourceResolver;
    }

    public void saveIssues(List<InspectCodeIssue> issues) {
        for (InspectCodeIssue issue : issues) {
            saveIssue(issue);
        }
    }

    private void saveIssue(InspectCodeIssue inspectCodeIssue) {
        File myResource = resourceResolver.getFile(inspectCodeIssue.getFile());
        if(myResource==null) {
            Log.debug("could not resolve " + inspectCodeIssue.getFile().getAbsolutePath());
            return;
        }
        Issuable issuable = perspectives.as(Issuable.class, myResource);
        if (issuable != null) {
            Issue issue = issuable
                    .newIssueBuilder()
                    .ruleKey(
                            RuleKey.of("ReSharperInspectCode",
                                    inspectCodeIssue.getTypeId())).line(10)
                    .message(inspectCodeIssue.getMessage()).build();
            issuable.addIssue(issue);
        }
    }
}
