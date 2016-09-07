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
package com.stevpet.sonar.plugins.dotnet.mscover.property;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Qualifiers;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class ExcludeProjectsProperty extends ConfigurationPropertyBase<List<String>> {
    
    public ExcludeProjectsProperty(Settings settings) {
        super(settings);
    }
    
    public ExcludeProjectsProperty() {
        super();
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(),PropertyType.STRING)
                .name("Exclude from run")
                .description("list of projects to exclude from test run")
                .multiValues(true).onlyOnQualifiers(Qualifiers.PROJECT);
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "excludeprojects";
    }

    /**
     * array of project names which should be excluded from run
     */
    @Override
    protected List<String> onGetValue(Settings settings) {
        List<String> projects= new ArrayList<String>();
        for(String project:settings.getStringArray(getKey())) {
            projects.add(project);
        }
        return projects;
    }

}
