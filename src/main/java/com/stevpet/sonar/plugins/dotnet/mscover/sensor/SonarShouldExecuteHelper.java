/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public class SonarShouldExecuteHelper implements ShouldExecuteHelper {
    
    static final Logger LOG = LoggerFactory
            .getLogger(SonarShouldExecuteHelper.class);
    
    private MsCoverProperties propertiesHelper;
    public SonarShouldExecuteHelper(MsCoverProperties propertiesHelper) {
        this.propertiesHelper = propertiesHelper;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.sensor.ShouldExecuteHelper#shouldExecuteOnProject(org.sonar.api.resources.Project)
     */
    public boolean shouldExecuteOnProject(Project project) {
        if(propertiesHelper.getRunMode() == RunMode.SKIP) {
            return false;
        }
        if (project == null) {
            LOG.error("MSCover : project is null, will not execute");
            return false;
            
        }
        return true;
    }

}
