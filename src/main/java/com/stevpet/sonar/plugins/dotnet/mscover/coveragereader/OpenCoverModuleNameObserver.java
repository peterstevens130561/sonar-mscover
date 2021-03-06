/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.coveragereader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs;
import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.ModuleNameObserver;

public class OpenCoverModuleNameObserver extends ModuleNameObserver {

	private final static Logger LOG = LoggerFactory
			.getLogger(OpenCoverModuleNameObserver.class);
	
    @Override
    public void registerObservers(TopLevelObserverRegistrar registrar) {
        registrar.inPath("Modules/Module").inElement("ModulePath", i -> { i.withEventArgs(this::moduleNameMatcher); } );
    }

	public void moduleNameMatcher(ParserEventArgs eventArgs) {
	    String value=eventArgs.getValue();
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
			eventArgs.setSkipTillNextElement("Module");
		}
	}


}
