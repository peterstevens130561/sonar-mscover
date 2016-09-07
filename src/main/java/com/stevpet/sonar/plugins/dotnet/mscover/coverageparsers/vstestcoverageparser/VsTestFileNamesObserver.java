/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser;

import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class VsTestFileNamesObserver extends VsTestCoverageObserver {
    private SonarCoverage registry;
    private String fileID;

    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("SourceFileNames").onElement("SourceFileID", this::sourceFileIDMatcher)
        .onElement("SourceFileName", this::sourceFileNameMatcher);
    }
    
    public void sourceFileIDMatcher(String value) {
        fileID = value;
    }

    public void sourceFileNameMatcher(String sourceFileName) {
        if (registry.fileIdExists(fileID)) {
            registry.linkFileNameToFileId(sourceFileName, fileID);
        }
    }

    @Override
    public void setVsTestRegistry(SonarCoverage registry) {
        this.registry = registry;
    }


}
