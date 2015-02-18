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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;


import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;

public class BinConfigAssemblyResolverTest {
    private AssemblyResolverTestUtils utils = new AssemblyResolverTestUtils();
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private String artifactName = "somename.dll";
    VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
    
    private AssemblyResolver assemblyResolver = new BinConfigAssemblyResolver();
    ProjectMock projectMock = new ProjectMock();
    
    @Before() 
    public void before() {
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
        utils.setAssemblyResolver(assemblyResolver);
        utils.setVisualStudioProject(visualStudioProjectMock.getMock());
    }
    @Test
    public void resolveAssembly_SimpleConfig_PathIncludesConfig() {

        utils.givenAssembly(null);

        String buildConfiguration="Reality";
        File directory = new File("hoi");
        
        visualStudioProjectMock.givenDirectory(directory);
        visualStudioProjectMock.givenArtifactName(artifactName);
        utils.setBuildConfiguration(buildConfiguration);
        utils.resolveAssembly();
        utils.verifyResolvedAs("hoi\\bin\\Reality\\somename.dll");

        
    }
}
