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

import java.io.File;
import java.util.List;

import org.sonar.api.resources.Project;

public interface CoverageSaver {

    /**
     * Save the line and block measures
     * @param project current project
     * @param path to the coverage.xml file
     * @param modules to parse
     */

    void analyse(Project project, String path,List<String>modules);

    /**
     * Save the line measures of a set of vsTest coverage xml files
     * @param project 
     * @param xmlFiles files to include
     * @param artifactNames in this project
     */
    void analyse(Project project, List<File> xmlFiles,
            List<String> artifactNames);

}