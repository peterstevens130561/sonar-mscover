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

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;
import static org.mockito.Mockito.mock;

public class OpenCoverCommandBuilderBuildTest {

    private OpenCoverCommandMock openCoverCommandMock = new OpenCoverCommandMock();
    private VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder = mock(VsTestRunnerCommandBuilder.class);
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    private List<String> assemblies;
    
    @Before() 
    public void before(){
        assemblies=new ArrayList<String>();
    }
    
    @Test()
    public void NoAssembly() {
        OpenCoverCommandBuilder builder = givenPreparedBuilder();
        builder.build();
        openCoverCommandMock.verifySetExcludeFromCodeCoverageAttributeFilter();
        openCoverCommandMock.verifyFilter("");
    }

    private OpenCoverCommandBuilder givenPreparedBuilder() {
        OpenCoverCommandBuilder builder = new OpenCoverCommandBuilder();
        builder.setOpenCoverCommand(openCoverCommandMock.getMock());
        builder.setAssemblies(assemblies);
        builder.setTestRunner(vsTestRunnerCommandBuilder);
        builder.setMsCoverProperties(msCoverPropertiesMock.getMock());
        builder.setTestEnvironment(vsTestEnvironmentMock.getMock());
        vsTestEnvironmentMock.givenXmlCoveragePath("somepath");
        return builder;
    }
    
    @Test
    public void OneAssembly_ExpectInFilter() {
        givenProject("assembly");
        OpenCoverCommandBuilder builder = givenPreparedBuilder();
        builder.build();
        openCoverCommandMock.verifySetExcludeFromCodeCoverageAttributeFilter();
        openCoverCommandMock.verifyFilter("+[assembly]* ");
    }
    
    @Test
    public void TwoAssemblies_ExpectInFilter() {
        givenProject("assembly1");
        givenProject("assembly2");
        OpenCoverCommandBuilder builder = givenPreparedBuilder();
        builder.build();
        openCoverCommandMock.verifySetExcludeFromCodeCoverageAttributeFilter();
        openCoverCommandMock.verifyFilter("+[assembly1]* +[assembly2]* ");
    }

    private void givenProject(String name) {
        assemblies.add(name);
    }
    

}
