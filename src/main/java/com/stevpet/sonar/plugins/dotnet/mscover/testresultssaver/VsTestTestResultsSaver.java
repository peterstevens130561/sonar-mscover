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
package com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.File;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.test.MutableTestPlan;
import org.sonar.api.test.TestCase;
import org.sonar.api.test.TestCase.Status;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public class VsTestTestResultsSaver  {
    private static final Logger LOG = LoggerFactory
            .getLogger(VsTestTestResultsSaver.class);
    TestResultsSaver testResultsSaver;
    SensorContext sensorContext;
    private ResourceResolver resourceResolver;
    private ResourcePerspectives perspectives;

    @SuppressWarnings("ucd")
    /**
     * With all dependencies, 
     * 
     * @param resourceResolver
     * @param testResultsFormatter
     */
	public
    VsTestTestResultsSaver(
            DefaultResourceResolver resourceResolver,ResourcePerspectives perspectives) {
        this.resourceResolver = resourceResolver;
        this.perspectives = perspectives;
    }
    
    /**
     * The default one
     * @param pathResolver
     * @param filesystem
     */
	public static VsTestTestResultsSaver create(
			PathResolver pathResolver, FileSystem filesystem, ResourcePerspectives perspectives) {
		return new VsTestTestResultsSaver(
			new DefaultResourceResolver(pathResolver,filesystem), perspectives
		);
	}

    public void save(@Nonnull SensorContext sensorContext,@Nonnull ProjectUnitTestResults projectUnitTestResults) {
        this.sensorContext=sensorContext;
        int saved = 0;
    	for (ClassUnitTestResult classUnitTestResult : projectUnitTestResults.values()) {
            java.io.File file = classUnitTestResult.getFile();
            if (file == null) {
                continue;
            }
            File sonarFile = resourceResolver.getFile(file);
            if (sonarFile == null) {
                continue;
            }

            saveFileSummaryResults(classUnitTestResult, sonarFile);
            saveFileTestResults(classUnitTestResult, sonarFile);
            saved += classUnitTestResult.getTests();
        }
    	LOG.debug("saved {} testresults",saved);
    }

    private void saveFileSummaryResults(ClassUnitTestResult fileResults,
            File sonarFile) {
        sensorContext.saveMeasure(sonarFile, CoreMetrics.SKIPPED_TESTS,
                fileResults.getIgnored());
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_ERRORS,
                (double) 0);
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_SUCCESS_DENSITY,
                fileResults.getDensity() * 100.0);
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_FAILURES,
                fileResults.getFail());
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_EXECUTION_TIME,
                fileResults.getLocalTimeMillis());
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TESTS,
                fileResults.getTests());
    }

 
    private void saveFileTestResults(ClassUnitTestResult fileResults, File sonarFile) {
        MutableTestPlan testplan = perspectives.as(MutableTestPlan.class, sonarFile);
        if(testplan == null) {
            return;
        }
        String testCaseType = sonarFile.getName().endsWith("feature.cs")?TestCase.TYPE_INTEGRATION:TestCase.TYPE_UNIT;
        fileResults.getUnitTests()
        .forEach( result ->
            testplan.addTestCase(result.getTestName())
            .setDurationInMs(result.getTimeInMicros()/1000)
            .setStackTrace(result.getStackTrace())
            .setMessage(result.getMessage())
            .setStatus(getStatus(result))
            .setType(testCaseType)
            );
    }
    
    private Status getStatus(UnitTestMethodResult result) {
        switch (result.getTestResult()) {
        case Passed: return Status.OK ;
        case Ignored: return Status.SKIPPED;
        case Failed : return Status.FAILURE;
        default : throw new IllegalArgumentException("Illegal test outcome " + result.getOutcome());
        }
    }

}