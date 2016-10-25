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
package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

class RunnableTestRunner implements Runnable {
    private Logger LOG = LoggerFactory.getLogger(RunnableTestRunner.class);
    private IntegrationTestRunner testRunner ;
    private String projectName;
    private Map<String, ProjectUnitTestResults> testResultsMap;
    
    RunnableTestRunner(IntegrationTestRunner testRunner, String projectName, Map<String, ProjectUnitTestResults> testResultsMap) {
        this.testRunner = testRunner;
        this.projectName=projectName;
        this.testResultsMap=testResultsMap;
    }
    
    @Override
    public void run() {

        testRunner.execute();
        ProjectUnitTestResults testResults = testRunner.getTestResults();
        synchronized(testResultsMap) {
            testResultsMap.put(projectName,testResults);
        }
        LOG.info("+++ tests on project {}",projectName);
    }
    
}
