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

import javax.annotation.Nonnull;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class TestRunnerTimeoutProperty extends ConfigurationPropertyBase<Integer> {
    private static final String PROPERTY_KEY = DefaultIntegrationTestsConfiguration.MSCOVER
            + "testrunner.timeout";
    
    /**
     * regular use
     * @param settings - valid settings
     */
    public TestRunnerTimeoutProperty(@Nonnull Settings settings) {
        super(settings);
    }
    
    /**
     * use only to get the PropertyBuilder
     */
    public TestRunnerTimeoutProperty() {
        super();
    }
    
    @Override
    public  Builder getPropertyBuilder() {
        return createProperty(PROPERTY_KEY, PropertyType.INTEGER)
                .name("Timeout for coverage reader")
                .description(
                        "Specifies max time in minutes that the testrunner may take to run all integrationtests in a solution")
                .defaultValue("120");
    }

    @Override
    public Integer onGetValue(Settings settings) {
        int timeout=0;
        try {
        timeout = settings.getInt(PROPERTY_KEY);
        } catch ( NumberFormatException e) {
            throw new InvalidPropertyValueException(PROPERTY_KEY,settings.getString(PROPERTY_KEY),"number >0 and <=120 minutes");
        }
        if(timeout < 0 || timeout > 480) {
            throw new InvalidPropertyValueException(PROPERTY_KEY, timeout, "number >0 and <=480 minutes");           
        }
        return timeout;
    }


    @Override
    public String getKey() {
        return PROPERTY_KEY;
    }

}
