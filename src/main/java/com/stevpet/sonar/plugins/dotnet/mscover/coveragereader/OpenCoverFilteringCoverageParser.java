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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragereader;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.FilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.ModuleNameObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class OpenCoverFilteringCoverageParser extends OpenCoverCoverageParser implements
		FilteringCoverageParser  {

    private final Logger LOG = LoggerFactory.getLogger(OpenCoverFilteringCoverageParser.class);

	private ModuleNameObserver moduleNameObserver;
	
	public OpenCoverFilteringCoverageParser(MsCoverConfiguration msCoverConfiguration) {
		super(msCoverConfiguration);
		moduleNameObserver= new OpenCoverModuleNameObserver();
		parser.registerObserver( moduleNameObserver);

	}

	@Override
	public void parse(SonarCoverage registry, File file) {
		super.parse(registry, file);
	}

	@Override
	public FilteringCoverageParser setModulesToParse(List<String> modules) {
        moduleNameObserver.addModulesToParse(modules);
        return this;
	}

}
