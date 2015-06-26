package com.stevpet.sonar.plugings.dotnet.resharper;

import java.util.List;

import org.sonar.api.component.Perspectives;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.internal.DefaultIssue;
import org.sonar.api.resources.File;
import org.sonar.api.rule.RuleKey;

public class InspectCodeIssuesSaver {

    private ResourcePerspectives perspectives;

    public InspectCodeIssuesSaver(ResourcePerspectives resourcePerspectives) {
        this.perspectives = resourcePerspectives;
    }

    public void saveIssues(List<InspectCodeIssue> issues) {
        for (InspectCodeIssue issue : issues) {
            saveIssue(issue);
        }
    }

    private void saveIssue(InspectCodeIssue inspectCodeIssue) {
        File myResource=null;
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
