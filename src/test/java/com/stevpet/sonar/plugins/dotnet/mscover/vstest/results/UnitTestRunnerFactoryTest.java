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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(UnitTestRunner.class)
public class UnitTestRunnerFactoryTest {
    
    private FileSystem fileSystem;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();

    @Before()
    public void before() {
        fileSystem=mock(FileSystem.class);
        microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);   
    }

    
    @Test
    public void noSolutionDefined_ExpectSonarException() {
        String workingDirPath="C:/Development/Rubbish/Solution/.sonar";
        File sonarWorkingDir=new File(workingDirPath);
        when(fileSystem.workDir()).thenReturn(sonarWorkingDir);
        VsTestRunner runner=new DefaultVsTestRunnerFactory().createBasicTestRunnner(msCoverPropertiesMock.getMock(), fileSystem, microsoftWindowsEnvironment);
        try {
            runner.prepareTestCommand();
        } catch (SonarException e) {
            assertEquals("No current solution",e.getMessage());
            return;
        }
        fail("expected SonarException, as there is no solution");
    }
    
    @Test
    public void sunnyDay_ExpectParametersCorrect() {
        String solutionPath="C:\\Development\\Rubbish\\Solution";
        File solutionDir=new File(solutionPath);
        String workingDirPath=solutionPath + "\\.sonar";
        
        File sonarWorkingDir=new File(workingDirPath);
        
        when(fileSystem.workDir()).thenReturn(sonarWorkingDir);
        
        VisualStudioSolution solution=mock(VisualStudioSolution.class);
        when(solution.getSolutionDir()).thenReturn(solutionDir);
        when(microsoftWindowsEnvironment.getCurrentSolution()).thenReturn(solution);
        VsTestRunner unitTestRunner=new DefaultVsTestRunnerFactory().createBasicTestRunnner(msCoverPropertiesMock.getMock(), fileSystem, microsoftWindowsEnvironment);
        assertEquals(workingDirPath,unitTestRunner.getSonarPath());
        assertEquals(solutionDir.getAbsolutePath(),unitTestRunner.getSolutionDirectory().getAbsolutePath());
        assertEquals(workingDirPath + "/coverage.xml",unitTestRunner.getCoverageXmlPath());
    }
    
    

}
