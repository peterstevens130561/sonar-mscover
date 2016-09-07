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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class AssemblyResolverTestUtils {

    private AssemblyResolver assemblyResolver;
    private File resultFile;
    private File assemblyFile;
    private String buildConfiguration;
    private VisualStudioProject visualStudioProject;

    /**
     * the resolver under test (mandatory
     * @param assemblyResolver
     */
    public void setAssemblyResolver(AssemblyResolver assemblyResolver) {

        this.assemblyResolver=assemblyResolver;
    }
    
    /**
     * The project (optional, depending on resolver)
     * @param mock
     */
    public void setVisualStudioProject(VisualStudioProject mock) {
        visualStudioProject = mock;
        
    }

    public  void givenAssembly(String fileName) {
        if(fileName!=null) {
            assemblyFile = new File(fileName);
        }
    }

    
    public void resolveAssembly() {
        resultFile=assemblyResolver.resolveAssembly(assemblyFile, visualStudioProject, buildConfiguration);
    }

    public void verifyShouldBeIgnored() {
        assertNull(resultFile);
    }
    
    public void verifyNotResolved() {
        assertEquals(assemblyFile,resultFile);
    }

    public void verifyResolvedAs(String string) {
        assertNotNull(resultFile);
        assertEquals(string.replaceAll("/","\\\\") ,resultFile.getPath());    
    }

    public void setBuildConfiguration(String buildConfiguration) {
        this.buildConfiguration=buildConfiguration;
    }


}