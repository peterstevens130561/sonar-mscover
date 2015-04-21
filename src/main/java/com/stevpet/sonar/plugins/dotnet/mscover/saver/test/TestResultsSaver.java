/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;

import com.stevpet.sonar.plugins.dotnet.mscover.model.TestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class TestResultsSaver {

    private MeasureSaver measureSaver;
    public TestResultsFormatter testResultsFormatter = new DefaultTestResultsFormatter() ;
    public TestResultsSaver(MeasureSaver measureSaver) {
        this.measureSaver = measureSaver;
    }

    void saveProjectTestResults(TestResults projectSummaryResults) {
        measureSaver.saveSummaryMeasure(CoreMetrics.TESTS,
                (double) projectSummaryResults.getExecutedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TEST_FAILURES,
                (double) projectSummaryResults.getFailedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TEST_ERRORS,
                (double) projectSummaryResults.getErroredTests());
    }

    public void saveSummaryMeasures(ClassUnitTestResult fileResults,
            ResourceSeam sonarFile) {
        sonarFile.saveMetricValue(CoreMetrics.SKIPPED_TESTS, (double) 0);
        sonarFile.saveMetricValue(CoreMetrics.TEST_ERRORS, (double) 0);
        sonarFile.saveMetricValue(CoreMetrics.TEST_SUCCESS_DENSITY,
                fileResults.getDensity() * 100.0);
        sonarFile.saveMetricValue(CoreMetrics.TEST_FAILURES,
                fileResults.getFail());
        sonarFile.saveMetricValue(CoreMetrics.TEST_EXECUTION_TIME, 1000.0);
        sonarFile.saveMetricValue(CoreMetrics.TESTS, fileResults.getTests());
    }

    public void saveTestCaseMeasures(ClassUnitTestResult fileResults,
            ResourceSeam sonarFile) {
        String data = testResultsFormatter.formatClassUnitTestResults(fileResults);
        Measure testData = new Measure(CoreMetrics.TEST_DATA, data);
        testData.setPersistenceMode(PersistenceMode.DATABASE);
        sonarFile.saveMeasure(testData);
    }
}
