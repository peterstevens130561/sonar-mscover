/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
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
