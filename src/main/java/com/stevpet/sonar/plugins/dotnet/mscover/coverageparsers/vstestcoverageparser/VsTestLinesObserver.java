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
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser;

import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class VsTestLinesObserver extends VsTestCoverageObserver {
    private boolean covered;
    private int line;
    private SonarCoverage registry;

    @Override
    public void setVsTestRegistry(SonarCoverage registry) {
        this.registry = registry;
    }

    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        registrar.inPath("Module/NamespaceTable/Class/Method/Lines", lines -> lines
            .onElement("LnStart", value ->line = Integer.parseInt(value) )
            .onElement("Coverage", value -> covered = "0".equals(value))
            .onElement("SourceFileID", value ->registry.getCoveredFile(value).addLinePoint(line, covered))
        );   
    }
    
}
