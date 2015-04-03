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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.VisualStudioProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class MicrosoftWindowsEnvironmentMock extends GenericClassMock<MicrosoftWindowsEnvironment>{
    private VisualStudioSolution solution = mock(VisualStudioSolution.class);

    public MicrosoftWindowsEnvironmentMock() {
        super(MicrosoftWindowsEnvironment.class);
    }

    public void givenHasNoTestProjects() {
        when(instance.getCurrentSolution()).thenReturn(solution);
        List<VisualStudioProject> value = new ArrayList<VisualStudioProject>();
        when(solution.getUnitTestProjects()).thenReturn(value);
    }
    

    public void givenHasTestProject() {
        when(instance.getCurrentSolution()).thenReturn(solution);
        List<VisualStudioProject> unitTestProjects = new ArrayList<VisualStudioProject>();
        VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
        VisualStudioProject vsProject = visualStudioProjectMock.getMock();
        unitTestProjects.add(vsProject);
        when(solution.getUnitTestProjects()).thenReturn(unitTestProjects);       
    }
    
    public void givenHasSolution(VisualStudioSolution solution) {
        when(instance.getCurrentSolution()).thenReturn(solution);
    }

    public void givenHasSolutionWithProject(int count) {
        when(instance.getCurrentSolution()).thenReturn(solution);
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        for(int projectIndex=0;projectIndex<count;projectIndex++) {
            VisualStudioProject vsProject = mock(VisualStudioProject.class);
            when(vsProject.getAssemblyName()).thenReturn("project" + projectIndex + ".dll");
            projects.add(vsProject);
        }
        when(solution.getUnitTestProjects()).thenReturn(projects);
        when(solution.getProjects()).thenReturn(projects);               // TODO Auto-generated method stub
        
    }


    public void givenHasAssemblies(List<String> assemblies) {
        when(instance.getAssemblies()).thenReturn(assemblies);
    }
    
}
