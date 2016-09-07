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

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stevpet.sonar.plugins.common.commandexecutor.TimeoutException;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public class CallableTestRunner implements Callable<Boolean> {

    private Logger LOG = LoggerFactory.getLogger(CallableTestRunner.class);
    private IntegrationTestRunner testRunner;
    private String projectName;
    private MultiThreadedSpecflowIntegrationTestCache testResultsMap;

    CallableTestRunner(IntegrationTestRunner testRunner, String projectName,
            MultiThreadedSpecflowIntegrationTestCache multiThreadedSpecFlowIntegrationTestCache) {
        this.testRunner = testRunner;
        this.projectName = projectName;
        this.testResultsMap = multiThreadedSpecFlowIntegrationTestCache;
    }

    /**
     * runs the tests, and saves the results
     */
    @Override
    public Boolean call() throws Exception {
        try {
            LOG.debug("+++ tests on project {} started", projectName);
            testRunner.execute();
            ProjectUnitTestResults testResults = testRunner.getTestResults();
            synchronized (testResultsMap) {
                LOG.debug("+++ tests on project {} writing coverage map", projectName);
                testResultsMap.put(projectName, testResults);
            }
            LOG.debug("+++ tests on project {} completed", projectName);
            return true;

        } catch (TimeoutException e) {
            LOG.error("Timeout occurred during {}",projectName);
            return false;
        }
            catch (Exception e) {
            LOG.error("Failed to execute tests on project {}\n message {}\n{}", projectName, e.getLocalizedMessage(), e
                    .getStackTrace().toString());
            throw e;
        }
    }
    
}
