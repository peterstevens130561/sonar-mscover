/*
 * Analysis Bootstrapper for Visual Studio Projects
 * Copyright (C) 2014 SonarSource
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
package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

/**
 * All information which can be extracted solely out of a .sln file.
 */
public class SimpleVisualStudioSolution implements VisualStudioSolution{

  private final List<VisualStudioSolutionProject> projects;
  private File solutionFile;
  private List<VisualStudioProject> unitTestVisualStudioProjects = new ArrayList<VisualStudioProject>();
  private List<VisualStudioProject> visualStudioProjects = new ArrayList<VisualStudioProject>();
  public SimpleVisualStudioSolution(File file, List<VisualStudioSolutionProject> projects) {
    this.projects = projects;
    this.solutionFile = file;
  }

  public List<VisualStudioSolutionProject> projects() {
    return projects;
  }

@Override
public File getSolutionDir() {
    return solutionFile.getParentFile();
}

@Override
public List<VisualStudioProject> getProjects() {
    return visualStudioProjects;
}

@Override
public List<VisualStudioProject> getUnitTestProjects() {
    return unitTestVisualStudioProjects;
}

@Deprecated
@Override
public VisualStudioProject getProjectFromSonarProject(Project project) {
    // TODO Auto-generated method stub
    return null;
}

@Deprecated
@Override
public VisualStudioProject getProject(File file) {
    // TODO Auto-generated method stub
    return null;
}


@Override
public void addVisualStudioProject(SimpleVisualStudioProject project) {
    visualStudioProjects.add(project); 
}

@Override
public void addUnitTestVisualStudioProject(SimpleVisualStudioProject project) {
    unitTestVisualStudioProjects.add(project);    // TODO Auto-generated method stub
    
}

@Override
public List<File> getUnitTestFiles() {
    List<File> unitTestFiles = new ArrayList<File>();
    for(VisualStudioProject project:getUnitTestProjects()) {
        unitTestFiles.addAll(project.getSourceFiles());
    }
    return unitTestFiles;
}

@Override
public List<String> getModules() {
    List<String> modules = new ArrayList<String>();
    for(VisualStudioProject project:getProjects()) {
        String name=project.getArtifactName();
        modules.add(name);
    }
    return modules;
}
}
