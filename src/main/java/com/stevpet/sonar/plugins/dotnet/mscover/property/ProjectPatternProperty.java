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

import java.util.regex.Pattern;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class ProjectPatternProperty extends ConfigurationPropertyBase<Pattern> {

    public ProjectPatternProperty(Settings settings) {
        super(settings);
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.REGULAR_EXPRESSION)
                .name("Pattern for integration test projects")
                .description(
                        "Regular expression to determine which projects are integration test projects, should also be included in overall test project pattern");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "projectpattern";
    }

    @Override
    protected Pattern onGetValue(Settings settings) {
        Pattern pattern = getPattern(getKey());
        return pattern;
    }
    
    

}
