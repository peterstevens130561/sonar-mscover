package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

public interface IntegrationTestsScheduler {
    /**
     * Checks whether tests should be done today. If multiple runs are done today, and today 
     * is the day on which they should run, then the tests should be run for each run.
     * @return true if tests should be done today
     */
    public boolean isScheduledDay();

}
