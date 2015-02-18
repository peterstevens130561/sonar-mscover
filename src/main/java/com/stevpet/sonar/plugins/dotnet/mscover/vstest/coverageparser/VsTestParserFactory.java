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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;

public interface VsTestParserFactory {

    /***
     * Creates the standard parser with all registries for the coverage file
     * @param registry
     * @deprecated use {@link #createCoverageParser(VsTestCoverageRegistry, List)} 
     * @return
     */
    XmlParserSubject createCoverageParser(
            VsTestCoverageRegistry registry);
    
    /**
     * Used to be able to find the sourcefile by specifying the method
     * @param map
     * @param sourceFileNamesRegistry
     * @return
     */
   XmlParserSubject createFileNamesParser(MethodToSourceFileIdMap map,
            SourceFileNameTable sourceFileNamesRegistry);

   /***
    * Creates the standard parser with all registries for the coverage file
    * @param registry
    * @param modules to parse
    */
    XmlParserSubject createCoverageParser(VsTestCoverageRegistry registry,
            List<String> modules);


}