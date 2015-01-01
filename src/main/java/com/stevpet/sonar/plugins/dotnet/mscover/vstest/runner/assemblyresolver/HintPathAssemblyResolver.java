/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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

import org.apache.commons.lang.StringUtils;

public class HintPathAssemblyResolver extends AssemblyResolverController
{  
    public void resolveAssembly(File projectDir, String assemblyName,
            String buildConfiguration) {
            String hintPath=getMsCoverProperties().getUnitTestHintPath();
            if(StringUtils.isEmpty(hintPath)) {
                return ;
            }
            File assemblyFile= new File(hintPath,assemblyName);
            environment.setAssembly(assemblyFile);
            
    }

}
