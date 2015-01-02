package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper;

public interface MicrosoftWindowsEnvironment {

    VisualStudioSolution getCurrentSolution();

    String getWorkingDirectory();

    boolean isTestExecutionDone();

    void setTestExecutionDone();

    VisualStudioProject getCurrentProject(String projectName);

}