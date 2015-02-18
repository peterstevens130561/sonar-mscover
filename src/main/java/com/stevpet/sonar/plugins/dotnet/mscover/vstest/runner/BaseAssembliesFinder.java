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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.AssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.BaseAssemblyResolver;

public class BaseAssembliesFinder extends AbstractAssembliesFinder implements AssemblyResolver{

    AssemblyResolver assemblyResolver = new BaseAssemblyResolver();
    public BaseAssembliesFinder(MsCoverProperties propertiesHelper) {
        super(propertiesHelper);
    }

    public File resolveAssembly(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {

        return null;
    }

    /**
     * Invoked from AbstractAssembliesFinder
     */
    public File searchNonExistingFile(File assemblyFile,
            VisualStudioProject project, String buildConfiguration) {

        return resolveChain(assemblyFile,project,buildConfiguration);
    }

    public File resolveChain(File assemblyFile, VisualStudioProject project,
            String buildConfiguration) {

        return assemblyResolver.resolveChain(assemblyFile, project, buildConfiguration);
    }

    public void setResolver(AssemblyResolver assemblyResolver) {
        this.assemblyResolver.setResolver(assemblyResolver);
    }

    public void setMsCoverProperties(MsCoverProperties msCoverProperties) {
        assemblyResolver.setMsCoverProperties(msCoverProperties);
    }

    public MsCoverProperties getMsCoverProperties() {
        return assemblyResolver.getMsCoverProperties();
    }




}
