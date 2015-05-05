package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public interface TestResultsSaver {
	void save(ProjectUnitTestResults projectUnitTestResults);
}
