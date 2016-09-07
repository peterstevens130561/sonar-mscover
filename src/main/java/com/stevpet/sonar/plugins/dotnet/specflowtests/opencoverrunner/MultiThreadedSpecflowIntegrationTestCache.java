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
package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.HashMap;
import java.util.Map;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

@InstantiationStrategy(value=InstantiationStrategy.PER_BATCH)
public class MultiThreadedSpecflowIntegrationTestCache implements BatchExtension {

    protected boolean didExecute;
    public boolean isDidExecute() {
        return didExecute;
    }

    public void setDidExecute(boolean didExecute) {
        this.didExecute = didExecute;
    }

    private Map<String,ProjectUnitTestResults> testResultsMap = new HashMap<>();

    public ProjectUnitTestResults getTestResults(String module) {
        synchronized(testResultsMap) {
        return testResultsMap.get(module);
        }
    }

    public boolean getDidExecute() {
        return didExecute;
    }

    public void put(String projectName, ProjectUnitTestResults testResults) {
        synchronized(testResultsMap) {
            testResultsMap.put(projectName, testResults);
        }
    }

}