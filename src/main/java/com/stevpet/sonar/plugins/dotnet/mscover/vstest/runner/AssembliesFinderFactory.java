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

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.AssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.BinConfigAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.FailedAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.HintPathAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.IgnoreMissingAssemblyResolver;

public class AssembliesFinderFactory implements AbstractAssembliesFinderFactory {

    public AssembliesFinder create(MsCoverProperties propertiesHelper) {
       AssemblyResolver[] assembliesFinders = {
               new FailedAssemblyResolver(),
               new IgnoreMissingAssemblyResolver(),
               new HintPathAssemblyResolver(),
               new BinConfigAssemblyResolver()
       };

       AssemblyResolver nextResolver=null;
        for(AssemblyResolver resolver: assembliesFinders) {
            resolver.setMsCoverProperties(propertiesHelper);
            resolver.setResolver(nextResolver);
            nextResolver=resolver;
        }
        
        BaseAssembliesFinder baseAssembliesFinder = new BaseAssembliesFinder(propertiesHelper);
        baseAssembliesFinder.setMsCoverProperties(propertiesHelper);
        baseAssembliesFinder.setResolver(nextResolver);
        return baseAssembliesFinder;
    }
}
