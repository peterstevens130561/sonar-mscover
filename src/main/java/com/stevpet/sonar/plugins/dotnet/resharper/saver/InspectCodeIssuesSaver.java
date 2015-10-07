package com.stevpet.sonar.plugins.dotnet.resharper.saver;

import java.util.List;

import org.sonar.api.BatchExtension;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;

public interface InspectCodeIssuesSaver extends BatchExtension{

    /**
     * @param issues non null issues list of issues which will be stored in SonarQube
     * <br>
     * Files in test projects should be ignored
     * <br>
     * Files outside solution should be ignored
     */
    void saveIssues(List<InspectCodeIssue> issues);

    /**
     * @param issues non null issues list of issues which will be stored in SonarQube
     * <br>
     * Files in test projects should be ignored
     * <br>
     * Files outside solution should be ignored
     */
    void saveModuleIssues(List<InspectCodeIssue> issues, Project module);

}