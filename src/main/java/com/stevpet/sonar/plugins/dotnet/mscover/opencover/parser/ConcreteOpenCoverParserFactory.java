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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverFileNamesAndIdObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;

public class ConcreteOpenCoverParserFactory implements OpenCoverParserFactory {
    /**
     * Creates the complete parser, with the observers registered
     * @param registry initialized registry
     * @deprecated {@link com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser}
     */
    @Deprecated
    public XmlParserSubject createOpenCoverParser(SonarCoverage registry) {
        XmlParserSubject parser = new OpenCoverParserSubject();
        OpenCoverObserver [] observers = { 
                new OpenCoverSourceFileNamesObserver(),
                new OpenCoverSequencePointsObserver(),
                new OpenCoverMissingPdbObserver()};
        for(OpenCoverObserver observer: observers) {
            observer.setRegistry(registry);
            parser.registerObserver(observer);
        }
        return parser;
    }

    public XmlParserSubject createOpenCoverFileNamesParser(
            MethodToSourceFileIdMap map,
            SourceFileNameTable sourceFileNamesRegistry) {
        XmlParserSubject parserSubject = new OpenCoverParserSubject();

        OpenCoverMethodObserver methodObserver = new OpenCoverMethodObserver();
        methodObserver.setRegistry(map);
        parserSubject.registerObserver(methodObserver);

        OpenCoverFileNamesAndIdObserver sourceFileNamesObserver = new OpenCoverFileNamesAndIdObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNamesRegistry);
        parserSubject.registerObserver(sourceFileNamesObserver);
        return parserSubject;
    }

    /**
     * Creates the complete parser, with the observers registered
     * @param registry initialized registry
     * @deprecated {@link com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser}
     */
    @Deprecated
    public XmlParserSubject createOpenCoverParser(SonarCoverage registry,
            MsCoverProperties msCoverProperties) {
        XmlParserSubject parser = new OpenCoverParserSubject();
        Collection<String> pdbsThatCanBeIgnoredWhenMissing = msCoverProperties.getPdbsThatMayBeIgnoredWhenMissing();
        OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs  missingPdbObserver = new OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs() ;
        missingPdbObserver.setPdbsThatCanBeIgnoredIfMissing(pdbsThatCanBeIgnoredWhenMissing);
        OpenCoverObserver [] observers = { 
                new OpenCoverSourceFileNamesObserver(),
                new OpenCoverSequencePointsObserver(),
                missingPdbObserver};
        for(OpenCoverObserver observer: observers) {
            observer.setRegistry(registry);
            parser.registerObserver(observer);
        }
        return parser;
    }
}
