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
package com.stevpet.sonar.plugins.dotnet.mscover.property;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;


public class ScheduleProperty extends ConfigurationPropertyBase<Pattern> {

    public ScheduleProperty(Settings settings) {
        super(settings);
    }
    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.REGULAR_EXPRESSION)
                .name("Pattern to specify schedule")
                .description("Regular expression to specify the day on which integration tests are run\n" +
                        "1=MONDAY, 2=Tuesday i.e. [6-7] = SATURDAY and SUNDAY")
                .defaultValue(".*");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER + "schedule";
    }

    @Override
    protected Pattern onGetValue(Settings settings) {
        String schedule = settings.getString(getKey());
        if ("*".equals(schedule)) {
            schedule = ".*";
        }
        try {
            Pattern p = Pattern.compile(schedule);
            return p;
        } catch (PatternSyntaxException e) {
            throw new InvalidPropertyValueException(getKey(), schedule, "valid regular expression", e);
        }
    }

}
