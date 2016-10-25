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

public class TestCaseFilterProperty extends ConfigurationPropertyBase<String> {


    public TestCaseFilterProperty(Settings settings) {
        super(settings);
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(),PropertyType.STRING).name("Testcasefilter").description("filter to apply to VSTEST for testcases");
    }


    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "testcasefilter";
    }

    @Override
    protected String onGetValue(Settings settings) {
        return settings.getString(getKey());
    }

}
