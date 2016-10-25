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

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class TestRunnerThreadsProperty extends ConfigurationPropertyBase<Integer> {

    private static final String PRECONDITION = "integer >0 and <=10";

    public TestRunnerThreadsProperty(Settings settings) {
        super(settings);
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(),PropertyType.INTEGER)
        .name("Number of threads for integrationtestrunner")
        .description(
                "Specified number of threads that the integrationtestrunner uses when reading the coverage data for a project")
        .defaultValue("1");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "testrunner.threads";
    }

    @Override
    protected Integer onGetValue(Settings settings) {
        int threads=0;
        try {
            threads = settings.getInt(getKey());
        } catch (NumberFormatException e) {
            throw new InvalidPropertyValueException(getKey(), settings.getString(getKey()), PRECONDITION);
        }
        if (threads < 0 || threads > 10) {
            throw new InvalidPropertyValueException(getKey(), settings.getString(getKey()), PRECONDITION);
        }
        return threads;
    }

}
