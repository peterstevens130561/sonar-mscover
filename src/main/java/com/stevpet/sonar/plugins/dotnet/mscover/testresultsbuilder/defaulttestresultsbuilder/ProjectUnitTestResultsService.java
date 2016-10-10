package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public interface ProjectUnitTestResultsService {

    ProjectUnitTestResults mapUnitTestResultsToFile();

}
