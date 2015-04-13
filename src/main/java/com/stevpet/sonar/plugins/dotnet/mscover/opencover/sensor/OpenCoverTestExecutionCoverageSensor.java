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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.Set;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.ConstructorInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.DotNetConstants;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.AbstractDotNetSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;  
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.OpenCoverWorkflowSteps;
@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
@DependedUpon("OpenCoverRunningVsTest")
public class OpenCoverTestExecutionCoverageSensor extends AbstractDotNetSensor {

    private static String WONT_EXECUTE = "VsTest.Console using OpenCover.Console.Exe won't execute as ";
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverTestExecutionCoverageSensor.class);
    private final MsCoverProperties propertiesHelper ;
    private VsTestEnvironment vsTestEnvironment;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;
    private DefaultPicoContainer openCoverContainer;

    private OpenCoverWorkflowSteps steps = new OpenCoverWorkflowSteps();
    private OpenCoverDirector openCoverDirector = new OpenCoverDirector(steps);
    public OpenCoverTestExecutionCoverageSensor(MsCoverProperties propertiesHelper, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            FileSystem fileSystem,
            VsTestEnvironment testEnvironment) {
        super(microsoftWindowsEnvironment, propertiesHelper.getMode());
        this.propertiesHelper = propertiesHelper;
        this.fileSystem = fileSystem;
        this.vsTestEnvironment = testEnvironment;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }

    @Override
    public String[] getSupportedLanguages() {
        return new String[] {"cs"};
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldExecuteOnProject(Project project) {
        if (vsTestEnvironment.getTestsHaveRun()) {
            logReasonToNotExecute("test execution has already been done.");
            return false;
        }
        if (getMicrosoftWindowsEnvironment().getCurrentSolution() != null
                && getMicrosoftWindowsEnvironment().getCurrentSolution()
                        .getUnitTestProjects().isEmpty()) {
            logReasonToNotExecute("there are no test projects.");
            return false;
        }
        Set<String> languages = fileSystem.languages();
        if ( !languages.contains("cs")) {
            return false;
        }
        if (propertiesHelper.runOpenCover()) {
            LOG.info("will run opencover with vstest embedded");
            return true;
        }
        return false;
    }
    private void logReasonToNotExecute(String string) {
        LOG.info(WONT_EXECUTE + string);
    }

    @Override
    public void analyse(Project project, SensorContext context) {
        wire();
        openCoverDirector.wire(openCoverContainer);
        vsTestEnvironment.setCoverageXmlFile(project,"coverage-report.xml"); 
        openCoverDirector.execute(); 
    }

    private void wire() {
        openCoverContainer = new DefaultPicoContainer(new ConstructorInjection());
        openCoverContainer.addComponent(propertiesHelper)
        .addComponent(vsTestEnvironment)
        .addComponent(microsoftWindowsEnvironment)
        .addComponent(fileSystem);
    }



}
