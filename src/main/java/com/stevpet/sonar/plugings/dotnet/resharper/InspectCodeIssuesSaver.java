package com.stevpet.sonar.plugings.dotnet.resharper;

import java.util.List;

import org.sonar.api.BatchExtension;

public interface InspectCodeIssuesSaver extends BatchExtension{

    /**
     * @param non null issues list of issues which will be stored in SonarQube
     */
    abstract void saveIssues(List<InspectCodeIssue> issues);

}