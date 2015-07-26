package com.stevpet.sonar.plugings.dotnet.resharper.saver;

import java.util.List;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugings.dotnet.resharper.InspectCodeIssue;

public interface InspectCodeIssuesSaver extends BatchExtension{

    /**
     * @param issues non null issues list of issues which will be stored in SonarQube
     * <br>
     * Files in test projects should be ignored
     * <br>
     * Files outside solution should be ignored
     */
    abstract void saveIssues(List<InspectCodeIssue> issues);

}