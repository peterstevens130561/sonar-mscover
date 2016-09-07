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
package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;

public interface CoverageFileLocator {


    /**
     * When running tests for a project, then the coverage file will be split per assembly. Each file must have a unique name. 
     * In the saving phase we want to find them per project, and the project names are equal to the assemblyName, so the created path
     * is the concatenation of root, assemblyName and project, the latter just to make it unique.
     * @param root 
     * @param projectName - the project currently being analysed
     * @param assemblyName - the covered assembly (module in the coverage file)
     * @return concatenation of root, assembly, project
     */
    File getFile(File root, String projectName, String assemblytName);

    /**
     * 
     * get the directory where all coverage files of a project are stored
     * @param root
     * @param projectName
     * @return
     */
    File getProjectDir(File root, String assemblytName);
}