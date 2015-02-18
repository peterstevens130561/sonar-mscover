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
package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;

public interface ResourceMediator {

    /**
     * Connect the saver to context, project and registry
     */

    void setDateFilter(DateFilter dateFilter);

    void setResourceFilter(ResourceFilter resourceFilter);

    ResourceSeam getSonarResource(SensorContext sensorContext, Project project,
            File file);

}