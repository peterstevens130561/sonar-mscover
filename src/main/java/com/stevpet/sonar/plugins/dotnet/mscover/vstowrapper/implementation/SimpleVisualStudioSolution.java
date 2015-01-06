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

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

/**
 * All information which can be extracted solely out of a .sln file.
 */
public class SimpleVisualStudioSolution implements VisualStudioSolution{

  private final List<VisualStudioSolutionProject> projects;

  public SimpleVisualStudioSolution(List<VisualStudioSolutionProject> projects) {
    this.projects = projects;
  }

  public List<VisualStudioSolutionProject> projects() {
    return projects;
  }

}
