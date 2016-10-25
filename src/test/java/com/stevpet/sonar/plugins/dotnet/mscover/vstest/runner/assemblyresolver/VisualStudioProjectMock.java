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

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

import static org.mockito.Mockito.when;

public class VisualStudioProjectMock extends GenericClassMock<VisualStudioProject> {
 
    public VisualStudioProjectMock() {
        super(VisualStudioProject.class);
    }

    public void givenDirectory(File directory) {
       when(instance.getDirectory()).thenReturn(directory);
    }

    public void givenArtifactName(String artifactName) {
        when(instance.getArtifactName()).thenReturn(artifactName);
    }

    public void givenIsUnitTest(boolean b) {
        when(instance.isUnitTest()).thenReturn(b);
    }

    public void givenArtifact(String buildConfiguration, String buildPlatform,
            String artifact) {
        when(instance.getArtifactFile()).thenReturn(new File(artifact));
    }
    
    public void givenArtifact(String buildConfiguration, String buildPlatform,
            File artifactFile) {
        when(instance.getArtifactFile()).thenReturn(artifactFile);
    }
    /**
     * wraps getArtifactName
     * @param artifactFile
     */
    public void givenArtifactName(File artifactFile) {
        String name= artifactFile==null?null:artifactFile.getName();
        when(instance.getArtifactName()).thenReturn(name);
    }

    public void givenAssemblyName(String name) {
        when(instance.getAssemblyName()).thenReturn(name);
    }
}
