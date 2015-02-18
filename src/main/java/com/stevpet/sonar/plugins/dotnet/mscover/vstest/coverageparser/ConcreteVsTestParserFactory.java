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


import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.ModuleNameObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestLinesToCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestMethodToSourceFileIdMapObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestSourceFileNamesToCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestSourceFileNamesToSourceFileNamesObserver;


public class ConcreteVsTestParserFactory implements VsTestParserFactory {
    
    @Override
    public XmlParserSubject createCoverageParser(VsTestCoverageRegistry registry) {
        return createCoverageParser(registry,new ArrayList<String>());
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.CoverageParserFactory#createDefault(com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry, com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry)
     */
    public XmlParserSubject createCoverageParser(VsTestCoverageRegistry registry,List<String> modules) {

        ModuleNameObserver moduleNameObserver = new ModuleNameObserver();
        moduleNameObserver.addModulesToParse(modules);
        XmlParserSubject parserSubject = new CoverageParserSubject();

        VsTestCoverageObserver[] observers = {

                new VsTestSourceFileNamesToCoverageObserver(),
                new VsTestLinesToCoverageObserver(),
                new VsTestSourceFileNamesToSourceFileNamesObserver(),
                moduleNameObserver
        };
        
        for(VsTestCoverageObserver observer : observers) {
            observer.setVsTestRegistry(registry);
            parserSubject.registerObserver(observer);            
        }
        return parserSubject;
    }

    /**
     * Used to be able to find the sourcefile by specifying the method
     * @param map
     * @param sourceFileNamesRegistry
     * @return
     */
    public XmlParserSubject createFileNamesParser(MethodToSourceFileIdMap map,
            SourceFileNameTable sourceFileNamesRegistry) {

        XmlParserSubject parserSubject = new CoverageParserSubject();

        VsTestMethodToSourceFileIdMapObserver methodObserver = new VsTestMethodToSourceFileIdMapObserver();
        methodObserver.setRegistry(map);
        parserSubject.registerObserver(methodObserver);

        VsTestSourceFileNamesToSourceFileNamesObserver sourceFileNamesObserver = new VsTestSourceFileNamesToSourceFileNamesObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        return parserSubject;
    }
 
}
