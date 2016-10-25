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
package com.stevpet.sonar.plugins.dotnet.mscover;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.property.ConfigurationPropertyBase;
import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class TestRunnerRetriesProperty extends ConfigurationPropertyBase<Integer> {
    private static final String PRECONDITION = "must be int > -1";
    
    public TestRunnerRetriesProperty(Settings settings) {
        super(settings);
    }
    
    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(),PropertyType.INTEGER)
        .name("Number of retries for integrationtestrunner")
        .description(
                "Specified number of retries that the integrationtestrunner will perform on a project in case of a timeout")
        .defaultValue("3");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "testrunner.retries";
    }

    @Override
    protected Integer onGetValue(Settings settings) {
        int retries=0;
        try {
            retries = settings.getInt(getKey());
        } catch (NumberFormatException e) {
            throw new InvalidPropertyValueException(getKey(), settings.getString(getKey()), PRECONDITION);
        }
        if (retries < 0) {
            throw new InvalidPropertyValueException(getKey(), settings.getString(getKey()), PRECONDITION);
        }
        return retries;
    }
}
