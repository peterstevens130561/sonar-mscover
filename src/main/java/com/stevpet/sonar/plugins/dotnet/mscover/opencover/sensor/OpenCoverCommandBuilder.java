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

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerCommandBuilder;

/**
 * Build the openCoverCommand through direct communication with the objects that know
 * 
 * @author stevpet
 *
 */
public class OpenCoverCommandBuilder {

    private OpenCoverCommand openCoverCommand;
    private MsCoverProperties msCoverProperties;
    private VsTestRunnerCommandBuilder unitTestRunner;
    private VsTestEnvironment testEnvironment;
    private List<String> assemblies;
    
    public OpenCoverCommandBuilder() {

    }
    
    public OpenCoverCommandBuilder setOpenCoverCommand(OpenCoverCommand openCoverCommand) {
        this.openCoverCommand = openCoverCommand;
        return this;
    }
    
 
    public OpenCoverCommandBuilder setMsCoverProperties(MsCoverProperties msCoverProperties) {
        this.msCoverProperties=msCoverProperties;
        return this;
    }
    
    public OpenCoverCommandBuilder setTestRunner(VsTestRunnerCommandBuilder unitTestRunnerCommandBuilder) {
        this.unitTestRunner = unitTestRunnerCommandBuilder;
        return this;
    }
    
    public OpenCoverCommandBuilder setTestEnvironment(
            VsTestEnvironment testEnvironment) {
        this.testEnvironment = testEnvironment;
        return this;
    }
    
    public void build() {
        VSTestCommand testCommand=unitTestRunner.build(false);
        openCoverCommand.setTargetCommand(testCommand);
        
        String path=msCoverProperties.getOpenCoverInstallPath();
        openCoverCommand.setCommandPath(path);
        
        List<String> excludeFilters = new ArrayList<String>();
        excludeFilters.add("*\\*.Designer.cs");
        openCoverCommand.setExcludeByFileFilter(excludeFilters);
        
        openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();
        String filter = getAssembliesToIncludeInCoverageFilter();
        openCoverCommand.setFilter(filter); 
        openCoverCommand.setRegister("user");
        openCoverCommand.setMergeByHash();
        openCoverCommand.setOutputPath(testEnvironment.getXmlCoveragePath());  
        if(msCoverProperties.getOpenCoverSkipAutoProps()) {
            openCoverCommand.setSkipAutoProps();
        }
    }
    

    public void setAssemblies(List<String> assemblies) {
        this.assemblies = assemblies;
    }
    public String getAssembliesToIncludeInCoverageFilter() {
        final StringBuilder filterBuilder = new StringBuilder();
        // We add all the covered assemblies
        for (String assemblyName : listCoveredAssemblies()) {
          filterBuilder.append("+[" + assemblyName + "]* ");
        }
        return filterBuilder.toString();
    }
    
    public List<String> listCoveredAssemblies() {
        return assemblies;
      }

}

