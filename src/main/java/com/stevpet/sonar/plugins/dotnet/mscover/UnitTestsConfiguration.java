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
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.Collection;

interface UnitTestsConfiguration {

	public enum Mode {
		DISABLED,
		ENABLED
	}
	
	public enum Tool {
		OPENCOVER,
		VSTEST
	}
	
	Mode getMode();
	
    String getUnitTestCoveragePath();


    String getUnitTestsAssemblies();

    String getTestSettings();



    /**
     * Gets the array of unit tests dlls that can be ignored if missing (A warning will still occur)
     * @return
     */
    Collection<String> getUnitTestAssembliesThatCanBeIgnoredIfMissing();

    Collection<String> getPdbsThatMayBeIgnoredWhenMissing();

    boolean isIgnoreMissingUnitTestAssembliesSpecified();

    String getOpenCoverInstallPath();

    String getUnitTestHintPath();
    
    boolean getOpenCoverSkipAutoProps();

    /**
     * @return path to installation dir of vstest.console.exe
     */
    String getVsTestInstallPath();
	
}
