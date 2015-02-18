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

import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;

import static org.mockito.Mockito.verify;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;

public class AssemblyResolverControllerTest {
    @Test
    public void test() {
        MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
        
        AssemblyResolver assemblyResolver = new ConcreteAssemblyResolverController() ;
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());

        File assemblyFile = null;
        VisualStudioProject project = null;
        String buildConfiguration=null;
        File result=assemblyResolver.resolveChain(assemblyFile, project, buildConfiguration);
        assertNull(result);
    }

    @Test
    public void nonExisting() {
        MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
        
        AssemblyResolver assemblyResolver = new ConcreteAssemblyResolverController() ;
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
        AssemblyResolverMock assemblyResolverMock = new AssemblyResolverMock();
        AssemblyResolver nextResolver = assemblyResolverMock.getMock();
        assemblyResolver.setResolver(nextResolver);
        File assemblyFile = new File("willnotexist");
        VisualStudioProject project = null;
        String buildConfiguration=null;
        File result=assemblyResolver.resolveChain(assemblyFile, project, buildConfiguration);
        assertNull(result);
        verify(nextResolver).resolveChain(assemblyFile, project, buildConfiguration);
    }
    private class ConcreteAssemblyResolverController extends AssemblyResolverController {

        public File resolveAssembly(File assemblyFile,
                VisualStudioProject project, String buildConfiguration) {
            // TODO Auto-generated method stub
            return assemblyFile;
        }

        
    }
}
