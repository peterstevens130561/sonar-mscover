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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

/**
 * Used to parse only those modules that are of interest to this project.
 * Especially for imported coverage files this saves a lot of time
 * 
 * @author stevpet
 * 
 */
public class VsTestModuleNameObserver extends VsTestCoverageObserver {
    private static final Logger LOG = LoggerFactory
            .getLogger(VsTestModuleNameObserver.class);
    private List<String> modulesToParse = new ArrayList<String>();

    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Module")
                .onElement("ModuleName",this::moduleNameMatcher);
        
    }
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.ModuleNameObserver#addModulesToParse(java.util.List)
	 */
	public void setModulesToParse(List<String> modules) {
        if (modules == null) {
            return;
        }
        for (String module : modules) {
            modulesToParse.add(module.toLowerCase());
        }
    }

    public void moduleNameMatcher(String value) {
        if (modulesToParse.isEmpty()) {
            return;
        }
        boolean shouldSkip = !modulesToParse.contains(value);
        if (!shouldSkip) {
            LOG.debug("Module {} will be parsed", value);
        }
        if (shouldSkip) {
            setSkipTillNextElement("Module");
        }
    }

    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.ModuleNameObserver#setVsTestRegistry(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage)
	 */
    @Override
	public void setVsTestRegistry(SonarCoverage registry) {
        // Ignoring intentionally
    }


}
