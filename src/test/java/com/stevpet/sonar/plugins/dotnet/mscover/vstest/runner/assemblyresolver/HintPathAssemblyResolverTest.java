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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;

import static org.mockito.Mockito.when ;

public class HintPathAssemblyResolverTest {
    @Mock MsCoverConfiguration msCoverConfiguration;
    VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
    HintPathAssemblyResolver assemblyResolver = new HintPathAssemblyResolver();
    AssemblyResolverTestUtils utils = new AssemblyResolverTestUtils();
    private String fileName = "unittest.dll";
    private String artifactName="artifactName";
    private String hintPath="C:/Development/Jewel.Release.Oahu/JewelEarth/bin";
    
    @Before()
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        assemblyResolver.setMsCoverProperties(msCoverConfiguration);
        
        utils.setAssemblyResolver(assemblyResolver);
        utils.givenAssembly(fileName);
        utils.setVisualStudioProject(visualStudioProjectMock.getMock());

        visualStudioProjectMock.givenArtifactName(artifactName);
    }
    
    @Test
    public void resolveAssembly_NoHintPathDefined_ReturnFile() {  
        utils.resolveAssembly();      
        utils.verifyNotResolved();    
    }
    
    @Test
    public void resolveAssembly_HitPathDefined_ReturnHintPath() {
        when(msCoverConfiguration.getUnitTestHintPath()).thenReturn(hintPath);
     
        utils.resolveAssembly(); 
        utils.verifyResolvedAs(hintPath + "\\" + artifactName);

    }
}
