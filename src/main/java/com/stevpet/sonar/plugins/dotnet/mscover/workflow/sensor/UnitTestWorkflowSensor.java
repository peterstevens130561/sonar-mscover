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
package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import org.picocontainer.DefaultPicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.common.commandexecutor.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.NullWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.OpenCoverWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.VsTestWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

/**
 * The new sensor which should ultimately cover all unit testing workflows.
 */
public class UnitTestWorkflowSensor extends WorkflowSensor {

    private static final Logger LOG = LoggerFactory
            .getLogger(UnitTestWorkflowSensor.class);
    private static final String LOGPREFIX = "UnitTestWorkflowSensor : ";
    private MsCoverConfiguration propertiesHelper;
    private VsTestEnvironment vsTestEnvironment;
    private WorkflowDirector workFlowDirector;
    private FileSystem fileSystem;
    private MicrosoftWindowsEnvironment microsoftWindowsenvironment;

    @SuppressWarnings("ucd")

    public UnitTestWorkflowSensor(VsTestEnvironment vsTestEnvironment,
            MsCoverConfiguration propertiesHelper, FileSystem fileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            PathResolver pathResolver, WorkflowDirector workflowDirector,ProcessLock processLock) {
        super(vsTestEnvironment, propertiesHelper, fileSystem, microsoftWindowsEnvironment, pathResolver, workflowDirector,processLock);
        this.propertiesHelper = propertiesHelper;
        this.vsTestEnvironment = vsTestEnvironment;
        this.workFlowDirector = workflowDirector;
        this.fileSystem=fileSystem;
        this.microsoftWindowsenvironment=microsoftWindowsEnvironment;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor.WorkflowSensor#analyse(org.sonar.api.resources.Project, org.sonar.api.batch.SensorContext)
     */
    @Override
    public void analyse(Project project, SensorContext context) {
        LogInfo("Starting");
        LogChanger.setPattern();
        executeUnitTests(project, context);

        LogInfo("Done");
    }


    private void executeUnitTests(Project project, SensorContext context) {
        DefaultPicoContainer childContainer = prepareChildContainer(context);
        vsTestEnvironment.setCoverageXmlFile(fileSystem, "coverage-report.xml");
        childContainer.addComponent(getWorkflow());
        workFlowDirector.wire(childContainer);
        workFlowDirector.execute();
    }

    private Class<? extends WorkflowSteps> getWorkflow() {
        Class<? extends WorkflowSteps> workflow = NullWorkflowSteps.class;
        RunMode runMode=propertiesHelper.getRunMode();
        workflow = VsTestWorkflowSteps.class;
        return workflow;
    }

    private void LogInfo(String msg, Object... objects) {
        LOG.info(LOGPREFIX + msg, objects);
    }

    @Override
    public boolean shouldExecuteWorkflow() {
        RunMode runMode=propertiesHelper.getRunMode();
        return microsoftWindowsenvironment.hasUnitTestSourceFiles() && !propertiesHelper.runOpenCover() && runMode== RunMode.RUNVSTEST;
    }

}
