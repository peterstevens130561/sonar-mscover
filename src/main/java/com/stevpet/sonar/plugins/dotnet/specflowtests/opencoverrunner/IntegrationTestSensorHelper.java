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

import java.util.regex.Pattern;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestSensorHelper {

    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private IntegrationTestsConfiguration integrationTestsConfiguration;


    public IntegrationTestSensorHelper(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            IntegrationTestsConfiguration integrationTestsConfiguration) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.integrationTestsConfiguration=integrationTestsConfiguration;
    }

    public boolean isSolutionWithIntegrationTestProjects(Project project) {
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        return project.isModule() && hasTestProjects(pattern) ;
    }

    private boolean hasTestProjects(Pattern pattern) {
        if(pattern==null) {
            return false;
        }
        return microsoftWindowsEnvironment.hasTestProjects(pattern);
    }

}
