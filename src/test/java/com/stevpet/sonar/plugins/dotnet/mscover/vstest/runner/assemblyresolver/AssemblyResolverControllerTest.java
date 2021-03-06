/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class AssemblyResolverControllerTest {
    @Mock private MsCoverConfiguration msCoverConfiguration ;
    
    @Test
    public void test() {
        org.mockito.MockitoAnnotations.initMocks(this);
        
        AssemblyResolver assemblyResolver = new ConcreteAssemblyResolverController() ;
        assemblyResolver.setMsCoverProperties(msCoverConfiguration);

        File assemblyFile = null;
        VisualStudioProject project = null;
        String buildConfiguration=null;
        File result=assemblyResolver.resolveChain(assemblyFile, project, buildConfiguration);
        assertNull(result);
    }

    @Test
    public void nonExisting() {
        AssemblyResolver assemblyResolver = new ConcreteAssemblyResolverController() ;
        assemblyResolver.setMsCoverProperties(msCoverConfiguration);
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
            return assemblyFile;
        }

        
    }
}
