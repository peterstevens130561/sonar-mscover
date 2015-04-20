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
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

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
    
    public static final String DEPENDS="VsTestSensor";
    private Logger Log= LoggerFactory
            .getLogger(WorkflowSensor.class);
    private final static String LOGPREFIX = "WorkflowSensor : ";
    private MsCoverProperties propertiesHelper;
    private DefaultPicoContainer container;
	private VsTestEnvironment vsTestEnvironment;
    
    public WorkflowSensor(VsTestEnvironment vsTestEnvironment, MsCoverProperties propertiesHelper,FileSystem fileSystem,MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        container = new DefaultPicoContainer(new ConstructorInjection());
        container.addComponent(vsTestEnvironment)
        .addComponent(fileSystem)
        .addComponent(microsoftWindowsEnvironment)
        .addComponent(propertiesHelper);
        this.propertiesHelper = propertiesHelper;
        this.vsTestEnvironment = vsTestEnvironment;
    }

    public boolean shouldExecuteOnProject(Project project) {
        return propertiesHelper.getRunMode() != RunMode.SKIP;
    }

    public void analyse(Project project, SensorContext context) {
    	LogInfo("Starting");
    	LogChanger.setPattern();
        container.addComponent(DefaultDirector.class)
                .addComponent(getWorkflow());
        WorkflowDirector  director=container.getComponent(WorkflowDirector.class);
        director.wire(container);
        vsTestEnvironment.setCoverageXmlFile(project,"coverage-report.xml"); 
        director.execute();  
        LogInfo("Done");
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

    private void LogInfo(String msg, Object ...objects ) {
        Log.info(LOGPREFIX + msg,objects);
    }

}
