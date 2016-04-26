package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

/***
 * Top layer of the integrationtest sensor
 * @author stevpet
 *
 */
public interface IntegrationTestRunnerApplication {

    /**
     * run the tests one in a batch. 
     * 
     * This will grab all of the projects that match the pattern, and execute them once on the first call (subsequent calls have no effect)
     */
    void execute();

    ProjectUnitTestResults getTestResults(String module);

}