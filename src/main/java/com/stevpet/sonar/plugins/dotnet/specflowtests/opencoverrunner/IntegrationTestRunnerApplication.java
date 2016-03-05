package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

/***
 * Top layer of the integrationtest sensor
 * @author stevpet
 *
 */
public interface IntegrationTestRunnerApplication {

    void execute();

    ProjectUnitTestResults getTestResults(String module);

}