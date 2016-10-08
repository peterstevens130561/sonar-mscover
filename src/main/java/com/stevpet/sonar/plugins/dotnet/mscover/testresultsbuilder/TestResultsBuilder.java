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
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import java.io.File;

import org.sonar.api.BatchExtension;

public interface TestResultsBuilder extends BatchExtension {
    /**
     * given the coverage file and the test results file, build a repository of
     * test results
     * @param valid
     *            testResultsFile
     * 
     * @return
     */
    ProjectUnitTestResults getTestResults();
    
    @Deprecated
    void parseCoverage(File coverageFile);
    
    void parseTestResults(File testResults);
}
