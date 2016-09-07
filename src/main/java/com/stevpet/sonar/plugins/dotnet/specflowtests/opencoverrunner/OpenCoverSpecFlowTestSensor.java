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

import java.util.regex.Pattern;

import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Mode;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Tool;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

/**
 * AS a developer I want to see analysis for solutions where the SpecFlow tests are part of the
 * solution itself. 
 * -tests are run first
 * -
 * @author stevpet
 *
 */
@DependedUpon(value="IntegrationTestCoverageSaved")
public class OpenCoverSpecFlowTestSensor implements Sensor {

    private final IntegrationTestsConfiguration integrationTestsConfiguration;
    private final OpenCoverSpecFlowTestSaverSensor saverSensor ;
    private final IntegrationTestRunnerApplication integrationTestRunnerApplication; 
    private final VsTestTestResultsSaver testResultsSaver;
    public OpenCoverSpecFlowTestSensor(IntegrationTestsConfiguration integrationTestsConfiguration, 
            IntegrationTestRunnerApplication integrationTestRunnerApplication, 
            OpenCoverSpecFlowTestSaverSensor saverSensor,
            VsTestTestResultsSaver testResultsSaver) {
        this.integrationTestsConfiguration = integrationTestsConfiguration;
        this.integrationTestRunnerApplication = integrationTestRunnerApplication;
        this.saverSensor = saverSensor;
        this.testResultsSaver = testResultsSaver;
    }

    public OpenCoverSpecFlowTestSensor(
            MsCoverConfiguration msCoverConfiguration, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            FileSystem fileSystem, 
            Settings settings, 
            PathResolver pathResolver,
            IntegrationTestsConfiguration integrationTestsConfiguration, 
            MultiThreadedSpecflowIntegrationTestCache multiThreadedSpecflowIntegrationTestCache,
            OpenCoverSpecFlowTestSaverSensor saverSensor, 
            ResourcePerspectives perspectives
            )
    {
        this.integrationTestRunnerApplication = new MultiThreadedSpecflowIntegrationTestApplication(microsoftWindowsEnvironment,
                integrationTestsConfiguration,
                msCoverConfiguration,
                fileSystem,
                settings,
                multiThreadedSpecflowIntegrationTestCache);
        this.testResultsSaver = VsTestTestResultsSaver.create(pathResolver, fileSystem,perspectives);
        this.integrationTestsConfiguration = integrationTestsConfiguration;
        this.saverSensor = saverSensor;
    }
    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return project.isModule() && integrationTestsConfiguration.matches(Tool.OPENCOVER, Mode.ACTIVE) ;
    }

    @Override
    public void analyse(Project module, SensorContext context) {


        integrationTestRunnerApplication.execute();

        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        
        String projectName = module.getName();
        boolean isTestProject=pattern.matcher(module.getName()).matches();
        if(isTestProject) {
            ProjectUnitTestResults testResults = integrationTestRunnerApplication.getTestResults(projectName);
            testResultsSaver.save(context, testResults);
        } else {
            saverSensor.analyse(module, context);
        }
        
    }

}
