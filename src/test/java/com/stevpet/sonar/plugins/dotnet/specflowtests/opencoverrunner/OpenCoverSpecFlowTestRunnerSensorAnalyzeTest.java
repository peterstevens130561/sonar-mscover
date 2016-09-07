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

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestCache;
import com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.OpenCoverSpecFlowTestRunnerSensor;

public class OpenCoverSpecFlowTestRunnerSensorAnalyzeTest {

	@Mock private MsCoverConfiguration configuration;
	@Mock private TestCache cache;
	@Mock private IntegrationTestRunnerApplication runner;
	private OpenCoverSpecFlowTestRunnerSensor sensor;
	@Mock private Project module;
	@Mock private SensorContext sensorContext;
	@Mock private VsTestTestResultsSaver testResultsSaver;
	@Mock private IntegrationTestsConfiguration integrationTestsConfiguration;
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);	
        sensor = new OpenCoverSpecFlowTestRunnerSensor(runner,testResultsSaver,integrationTestsConfiguration);
	}
	
	@Test
	public void test() {
		String projectName="projectName";
		File root = new File("root");
		ProjectUnitTestResults projectUnitTestResults = new ProjectUnitTestResults();
		when(integrationTestsConfiguration.getDirectory()).thenReturn(root);
		when(module.getName()).thenReturn(projectName);
		when(runner.getTestResults(projectName)).thenReturn(projectUnitTestResults);
		sensor.analyse(module, sensorContext);
		
		
		verify(runner,times(1)).execute();
		verify(testResultsSaver,times(1)).save(eq(sensorContext), eq(projectUnitTestResults));
	}
	
}
