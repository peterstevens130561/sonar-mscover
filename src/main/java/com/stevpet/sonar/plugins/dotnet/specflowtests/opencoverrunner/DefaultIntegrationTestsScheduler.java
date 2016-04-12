package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.Calendar;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;

public class DefaultIntegrationTestsScheduler implements IntegrationTestsScheduler {
    private final IntegrationTestsConfiguration integrationTestsConfiguration;    

    public DefaultIntegrationTestsScheduler(IntegrationTestsConfiguration integrationTestsConfiguration) {

        this.integrationTestsConfiguration = integrationTestsConfiguration;
    }

    @Override
    public boolean isScheduledDay() {
        Pattern schedulePattern=integrationTestsConfiguration.getSchedule();
        String dayOfWeek = getDayOfWeek().toString();
        return schedulePattern.matcher(dayOfWeek).matches();
    }
    
    /**
     * Can be overridden for tests
     * @return
     */
    protected Integer getDayOfWeek() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_WEEK);
        
    }

}
