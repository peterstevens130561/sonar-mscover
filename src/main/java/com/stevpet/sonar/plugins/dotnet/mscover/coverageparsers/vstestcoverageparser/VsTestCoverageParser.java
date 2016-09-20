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

import java.io.File;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class VsTestCoverageParser implements CoverageParser {
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.CoverageParser
     * #parser
     * (com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage,
     * java.io.File)
     */
    @Override
    public void parse(ProjectCoverageRepository registry, File file) {
        XmlParser xmlParser = new DefaultXmlParser();

        VsTestCoverageObserver[] observers = { new VsTestFileNamesObserver(),
                new VsTestLinesObserver() };

        for (VsTestCoverageObserver observer : observers) {
            observer.setVsTestRegistry(registry);
            xmlParser.registerObserver(observer);
        }
        xmlParser.parseFile(file);
    }
}
