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
import org.picocontainer.injectors.ConstructorInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest.VsTestIntegrationTestWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.InjectedMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.NullWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.OpenCoverWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.VsTestWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;

/**
 * The new sensor which should ultimately cover all unit testing workflows.
 */
public class WorkflowSensor implements Sensor {

	public static final String DEPENDS = "VsTestSensor";
	private static final Logger Log = LoggerFactory.getLogger(WorkflowSensor.class);
	private final static String LOGPREFIX = "WorkflowSensor : ";
	private MsCoverProperties propertiesHelper;
	private DefaultPicoContainer container;
	private VsTestEnvironment vsTestEnvironment;

	public WorkflowSensor(InjectedMeasureSaver injectedMeasureSaver,
			VsTestEnvironment vsTestEnvironment,
			MsCoverProperties propertiesHelper, FileSystem fileSystem,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			PathResolver pathResolver) {
		container = new DefaultPicoContainer(new ConstructorInjection());
		container.addComponent(vsTestEnvironment).addComponent(fileSystem)
				.addComponent(microsoftWindowsEnvironment)
				.addComponent(propertiesHelper)
				.addComponent(injectedMeasureSaver).addComponent(pathResolver);
		this.propertiesHelper = propertiesHelper;
		this.vsTestEnvironment = vsTestEnvironment;
	}

	public boolean shouldExecuteOnProject(Project project) {
		return propertiesHelper.getRunMode() != RunMode.SKIP;
	}

	public void analyse(Project project, SensorContext context) {
		LogInfo("Starting");
		LogChanger.setPattern();
		executeUnitTests(project, context);
		executeIntegrationTests(project, context);
		LogInfo("Done");
	}

	private void executeIntegrationTests(Project project, SensorContext context) {
		DefaultPicoContainer childContainer = prepareChildContainer(context);
		childContainer.addComponent(VsTestIntegrationTestWorkflowSteps.class);
		WorkflowDirector director = childContainer
				.getComponent(WorkflowDirector.class);
		director.wire(childContainer);
		vsTestEnvironment.setCoverageXmlFile(project, "coverage-report.xml");
		director.execute();
	}

	private void executeUnitTests(Project project, SensorContext context) {
		DefaultPicoContainer childContainer = prepareChildContainer(context);
		childContainer.addComponent(getWorkflow());
		WorkflowDirector director = childContainer
				.getComponent(WorkflowDirector.class);
		director.wire(childContainer);
		vsTestEnvironment.setCoverageXmlFile(project, "coverage-report.xml");
		director.execute();
	}

	private DefaultPicoContainer prepareChildContainer(SensorContext context) {
		DefaultPicoContainer childContainer = new DefaultPicoContainer();
		container.addChildContainer(childContainer);
		childContainer.addComponent(DefaultDirector.class)
				.addComponent(context);
		return childContainer;
	}

	private Class<? extends WorkflowSteps> getWorkflow() {
		Class<? extends WorkflowSteps> workflow = NullWorkflowSteps.class;
		if (propertiesHelper.getRunMode() == RunMode.RUNVSTEST) {
			if (propertiesHelper.runOpenCover()) {
				workflow = OpenCoverWorkflowSteps.class;
			} else {
				workflow = VsTestWorkflowSteps.class;
			}
		}
		return workflow;
	}

	private void LogInfo(String msg, Object... objects) {
		Log.info(LOGPREFIX + msg, objects);
	}

}
