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

import java.io.File;
import org.sonar.api.PropertyType;
import org.sonar.api.config.Settings;
import org.sonar.api.config.PropertyDefinition.Builder;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class CoverageRootProperty extends ConfigurationPropertyBase<File> {
    
    public CoverageRootProperty(Settings settings) {
        super(settings);
    }
    
    public CoverageRootProperty() {
        super();
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(), PropertyType.STRING)
                .name("root directory of integration test projects")
                .description("used in auto mode to determine whether to execute integration tests or save coverage data");
    }

    @Override
    public File onGetValue(Settings settings) {
        String key=getKey();
        String coveragePath = settings.getString(key);
        if(coveragePath == null) {
            throw new InvalidPropertyValueException(key, "property is required, and parent must exist");
        }
        File coverageDir = new File(coveragePath);
        File parentDir=coverageDir.getParentFile();
        if(parentDir==null) {
            throw new InvalidPropertyValueException(key, coveragePath,"must have parent");
        }
        if(!parentDir.exists()) {
            throw new InvalidPropertyValueException(key, coveragePath,"parent must exist");            
        }
        return coverageDir;
    }


    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER + "dir";
    }
    

}
