package com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public interface TestResultsSaver extends BatchExtension{
    void save(ProjectUnitTestResults projectUnitTestResults);
}
