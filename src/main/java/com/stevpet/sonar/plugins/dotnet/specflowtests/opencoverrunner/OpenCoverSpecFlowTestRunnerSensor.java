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
package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Mode;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Tool;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.IntegrationTestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

/**
 * ProjectBuilder for dotnet projects
 * 
 * The build method will be invoked by sonar in the ProjectBuild phase, and
 * populates the MicrosoftWindowsEnvironment
 * 
 * @author stevpet
 * 
 */
public class OpenCoverSpecFlowTestRunnerSensor implements Sensor {

	private final IntegrationTestRunnerApplication integrationTestRunnerApplication;
	private final VsTestTestResultsSaver testResultsSaver;
	private final IntegrationTestsConfiguration integrationTestsConfiguration;
	/**
	 * Create with all dependencies
	 * 
	 * @param fileSystem
	 * @param integrationTestRunnerApplication
	 * @param testResultsBuilder
	 * @param testResultsSaver
	 * @param coverageReader
	 * @param coverageSaver
	 * @param openCoverModuleSaver
	 * @param integrationTestsConfiguration
	 */

	public OpenCoverSpecFlowTestRunnerSensor(
			IntegrationTestRunnerApplication integrationTestRunnerApplication,
			VsTestTestResultsSaver testResultsSaver,
			IntegrationTestsConfiguration integrationTestsConfiguration,
			FileSystem fileSystem) {
		this.integrationTestRunnerApplication = integrationTestRunnerApplication;
		this.testResultsSaver = testResultsSaver;
		this.integrationTestsConfiguration = integrationTestsConfiguration;
	}
	
	/**
	 * Create with default dependencies.
	 * This one will be used by Sonar
	 * @param integrationTestCache
	 * @param msCoverConfiguration
	 * @param microsoftWindowsEnvironment
	 * @param fileSystem
	 * @param vsTestEnvironment
	 * @param settings
	 * @param pathResolver
	 * @param integrationTestsConfiguration
	 */
	public OpenCoverSpecFlowTestRunnerSensor(
			IntegrationTestCache integrationTestCache, 
			MsCoverConfiguration msCoverConfiguration, 
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
			FileSystem fileSystem, 
			VsTestEnvironment vsTestEnvironment, 
			Settings settings, 
			PathResolver pathResolver,
			IntegrationTestsConfiguration integrationTestsConfiguration
			) {
		this.integrationTestRunnerApplication = new MultiThreadedSpecflowIntegrationTestRunner(microsoftWindowsEnvironment,
		        integrationTestsConfiguration,new DefaultIntegrationTestRunnerFactory(integrationTestCache, msCoverConfiguration, microsoftWindowsEnvironment, fileSystem, vsTestEnvironment, settings)
				,fileSystem);
		this.testResultsSaver = VsTestTestResultsSaver.create(pathResolver, fileSystem);
		this.integrationTestsConfiguration = integrationTestsConfiguration;
	}


	private static final Logger LOG = LoggerFactory
			.getLogger(OpenCoverSpecFlowTestRunnerSensor.class);

	@Override
	public boolean shouldExecuteOnProject(Project project) {
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        return project.isModule() && pattern!=null && pattern.matcher(project.getName()).matches() &&
			 integrationTestsConfiguration.matches(Tool.OPENCOVER,
						Mode.RUN) ;
	}

	@Override
	public void analyse(Project module, SensorContext context) {

		String projectName = module.getName();
		integrationTestRunnerApplication.execute();

		ProjectUnitTestResults testResults = integrationTestRunnerApplication.getTestResults(projectName);
		testResultsSaver.save(context, testResults);
	}
}
