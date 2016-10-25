/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
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
