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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.ModuleNameObserver;

public class OpenCoverModuleNameObserver extends ModuleNameObserver {

	private final static Logger LOG = LoggerFactory
			.getLogger(OpenCoverModuleNameObserver.class);
	
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Modules/Module").onElement("ModulePath", this::moduleNameMatcher);
    }

	public void moduleNameMatcher(String value) {
		if (modulesToParse == null || modulesToParse.isEmpty()) {
			return;
		}
		int lastDirSep=value.lastIndexOf("\\");
		String moduleName;
		if(lastDirSep== -1) {
			moduleName=value;
		} else {
			moduleName=value.substring(lastDirSep+1);
		}
		boolean shouldSkip = !modulesToParse.contains(moduleName.toLowerCase());
		if (!shouldSkip) {
			LOG.debug("Module {} will be parsed", value);
		}
		if (shouldSkip) {
			setSkipTillNextElement("Module");
		}
	}


}
