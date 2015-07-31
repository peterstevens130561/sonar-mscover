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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public abstract class AssemblyResolverController implements AssemblyResolver {
    private static Logger LOG = LoggerFactory.getLogger(AssemblyResolverController.class);
    private AssemblyResolver assemblyResolver ;
    private MsCoverConfiguration msCoverProperties;

    public File resolveChain(File assemblyFile,VisualStudioProject project, String buildConfiguration) {
        LOG.debug("trying");
        File file=resolveAssembly(assemblyFile,project,buildConfiguration) ;
        if(file == null) {
            LOG.debug("Ignoring");
            return null;
        }
        if( file.exists()) {
            LOG.debug("Found {}",file.getAbsoluteFile());
            return file;
        }
        LOG.debug("Not found {}",file.getAbsolutePath());
        return assemblyResolver.resolveChain(assemblyFile,project,buildConfiguration);
    }


    public void setResolver(AssemblyResolver assemblyResolver) {
        this.assemblyResolver=assemblyResolver;
    }
    

    public void setMsCoverProperties(MsCoverConfiguration msCoverProperties) {
        this.msCoverProperties=msCoverProperties;
    }
    

    public MsCoverConfiguration getMsCoverProperties() {
        return msCoverProperties;
    }

}
