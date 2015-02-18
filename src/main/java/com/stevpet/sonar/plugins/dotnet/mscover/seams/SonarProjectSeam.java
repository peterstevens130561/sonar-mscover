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
package com.stevpet.sonar.plugins.dotnet.mscover.seams;

import java.io.File;

import org.sonar.api.resources.Project;


public class SonarProjectSeam   implements ProjectSeam{

    private Project project ;

    public SonarProjectSeam(Project project) {
        this.project = project ;
    }
    
    public SonarProjectSeam() {

    }

    public void setProject(Project project) {
        this.project=project;
    }
    public static SonarProjectSeam create(Project project) {
        return new SonarProjectSeam(project) ;
    }

    public boolean isRoot() {
        return project.isRoot();
    }

    @SuppressWarnings("deprecation")
    public File getSonarWorkingDirectory() {
        return project.getFileSystem().getSonarWorkingDirectory();
    }

    public File getSonarFile(String name) {
        File sonarWorkingDirectory=getSonarWorkingDirectory();
        return new File(sonarWorkingDirectory,name);
    }
}
