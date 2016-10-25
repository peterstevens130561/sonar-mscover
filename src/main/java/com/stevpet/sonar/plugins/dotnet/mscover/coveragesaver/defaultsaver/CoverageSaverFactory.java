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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;

public interface CoverageSaverFactory {

    /**
     * saver for unit test coverage data created with vstest (only lines)
     * @return 
     */
    CoverageSaver createVsTestUnitTestCoverageSaver();

    /**
     * saver for unit test coverage data created with opencover (lines and branches)
     */
    CoverageSaver createOpenCoverUnitTestCoverageSaver();

    /**
     * saver for integration test coverage data created with opencover (lines and branches)
     */
    CoverageSaver createOpenCoverIntegrationTestCoverageSaver();

}