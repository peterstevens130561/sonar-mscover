package com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public interface TestResultsSaver {
    void save(ProjectUnitTestResults projectUnitTestResults);
}
