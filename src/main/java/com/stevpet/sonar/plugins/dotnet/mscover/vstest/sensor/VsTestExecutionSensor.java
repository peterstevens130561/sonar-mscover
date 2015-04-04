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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

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
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

@DependedUpon(VsTestExecutionSensor.DEPENDS)
public class VsTestExecutionSensor implements Sensor {
    
    public static final String DEPENDS="VsTestSensor";
    private Logger Log= LoggerFactory
            .getLogger(VsTestExecutionSensor.class);
    private final static String LOGPREFIX = "MsCover/VsTestExecutionSensor : ";
    private VsTestEnvironment vsTestEnvironment;
    private FileSystem fileSystem;
    private MsCoverProperties propertiesHelper;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private DefaultPicoContainer container;
    private VsTestExecutionSensorDirector director = new VsTestExecutionSensorDirector();
 
    
    public VsTestExecutionSensor(VsTestEnvironment vsTestEnvironment, MsCoverProperties propertiesHelper,FileSystem fileSystem,MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.vsTestEnvironment = vsTestEnvironment;
        this.fileSystem = fileSystem;
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
        this.propertiesHelper = propertiesHelper;
    }

    public boolean shouldExecuteOnProject(Project project) {
        return propertiesHelper.runVsTest();
    }

    public void analyse(Project project, SensorContext context) {
        if(vsTestEnvironment.getTestsHaveRun()) {
            LogInfo("tests have run already");
            return;
        }
        
        if(!project.isRoot()) {
            LogInfo("MsCover/VsTestExecutionSensor won't execute as project is not root {}",project.getName());
            return;
        }
        
        LogInfo("MsCover/VsTestExecutionSensor : started running tests");
        wire();
        director.wire(container);
        vsTestEnvironment.setCoverageXmlFile(project,"coverage-report.xml"); 
        director.execute();  
    }

    private void wire() {
        container = new DefaultPicoContainer(new ConstructorInjection());
        container.addComponent(vsTestEnvironment)
        .addComponent(fileSystem)
        .addComponent(microsoftWindowsEnvironment)
        .addComponent(propertiesHelper);
    }
    

    private void LogInfo(String msg, Object ...objects ) {
        Log.info(LOGPREFIX + msg,objects);
    }

}
