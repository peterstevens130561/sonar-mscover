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

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class CoverageReaderThreadsProperty extends ConfigurationPropertyBase<Integer> {

    public CoverageReaderThreadsProperty(Settings settings) {
        super(settings);

    }

    public CoverageReaderThreadsProperty() {
        super();
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.INTEGER)
                .name("Number of threads for coverage reader")
                .description(
                        "Specified number of threads that the coverage reader uses when reading the coverage data for a project")
                .defaultValue("1");
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "coveragereader.threads";
    }

    @Override
    protected Integer onGetValue(Settings settings) {
        try {
            int threads = settings.getInt(getKey());
            if (threads <= 0 || threads >= 10) {
                throw new InvalidPropertyValueException(getKey(),settings.getString(getKey()),"must be integer between 1 and 10");
            }
            return threads;
        } catch (NumberFormatException e) {
            throw new InvalidPropertyValueException(getKey(),settings.getString(getKey()),"must be integer between 1 and 10");
        }
    }



}
