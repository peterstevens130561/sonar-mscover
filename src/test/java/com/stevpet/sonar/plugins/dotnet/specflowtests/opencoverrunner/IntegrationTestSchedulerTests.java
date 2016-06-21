package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;


import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;

public class IntegrationTestSchedulerTests {
    @Mock private IntegrationTestsConfiguration integrationTestsConfiguration ;
    private IntegrationTestsScheduler integrationTestScheduler ;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        when(integrationTestsConfiguration.getSchedule()).thenReturn(Pattern.compile(".*"));
        integrationTestScheduler = new StubbedIntegrationTestsScheduler(integrationTestsConfiguration);
    }
    
    @Test
    public void notSpecifiedRunAlwaysOnDay1() {
        runsOnDay(1);
    }
    
    @Test
    public void notSpecifiedRunAlwaysOnDay2() {
        runsOnDay(2);
    }

    @Test
    public void notSpecifiedRunAlwaysOnDay3() {
        runsOnDay(3);
    }

    @Test
    public void notSpecifiedRunAlwaysOnDay4() {
        runsOnDay(4);
    }
    
    @Test
    public void notSpecifiedRunAlwaysOnDay5() {
        runsOnDay(5);
    }
    
    @Test
    public void notSpecifiedRunAlwaysOnDay6() {
        runsOnDay(6);
    }
    
    @Test
    public void notSpecifiedRunAlwaysOnDay7() {
        runsOnDay(7);
    }
    private void runsOnDay(int dayOfWeek) {
        ((StubbedIntegrationTestsScheduler)integrationTestScheduler).setDayOfWeek(dayOfWeek);
        assertTrue("no filter specified, should run on each day this day is" + dayOfWeek,integrationTestScheduler.isScheduledDay());
    }
    
    
    private class StubbedIntegrationTestsScheduler extends DefaultIntegrationTestsScheduler {

        private int dayOfWeek;

        public StubbedIntegrationTestsScheduler(IntegrationTestsConfiguration integrationTestsConfiguration) {
            super(integrationTestsConfiguration);
        }

        @Override
        protected Integer getDayOfWeek() {
            return dayOfWeek;
        }
        public void setDayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }
        
    }
}
