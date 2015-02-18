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

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;


public interface AssemblyResolver  {

    File resolveChain(File assemblyFile, VisualStudioProject project,
            String buildConfiguration);

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssemblyResolver#setFinder(com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder)
     */
    void setResolver(AssemblyResolver assemblyResolver);

    void setMsCoverProperties(MsCoverProperties msCoverProperties);

    MsCoverProperties getMsCoverProperties();
    
    /**
     * 
     * @param assemblyFile
     * @param project
     * @param buildConfiguration
     * @return :
     * - null if resolver concludes that assembly can not be resolved, stop the chain. 
     * - existing file will stop the chain
     * - return non-existing file to continue
     */
    File resolveAssembly(File assemblyFile,VisualStudioProject project, String buildConfiguration);

}