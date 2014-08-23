package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;


import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sonar.api.config.Settings;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerFactory;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(UnitTestRunner.class)
public class UnitTestRunnerFactoryTest {
    
    private ModuleFileSystem moduleFileSystem;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private PropertiesHelper propertiesHelper;
    private Settings settings;

    @Before()
    public void before() {
        moduleFileSystem=mock(ModuleFileSystem.class);
        microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);   
        propertiesHelper=PropertiesHelper.create(settings);
    }

    
    @Test
    public void noSolutionDefined_ExpectSonarException() {
        String workingDirPath="C:/Development/Rubbish/Solution/.sonar";
        File sonarWorkingDir=new File(workingDirPath);
        when(moduleFileSystem.workingDir()).thenReturn(sonarWorkingDir);
        try {
        VsTestRunner unitTestRunner=VsTestRunnerFactory.createBasicTestRunnner(propertiesHelper, moduleFileSystem, microsoftWindowsEnvironment);
        } catch (SonarException e) {
            assertEquals(e.getMessage(),"No current solution");
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
        
        when(moduleFileSystem.workingDir()).thenReturn(sonarWorkingDir);
        
        VisualStudioSolution solution=mock(VisualStudioSolution.class);
        when(solution.getSolutionDir()).thenReturn(solutionDir);
        when(microsoftWindowsEnvironment.getCurrentSolution()).thenReturn(solution);
        VsTestRunner unitTestRunner=VsTestRunnerFactory.createBasicTestRunnner(propertiesHelper, moduleFileSystem, microsoftWindowsEnvironment);
        assertEquals(workingDirPath,unitTestRunner.getSonarPath());
        assertEquals(solutionDir.getAbsolutePath(),unitTestRunner.getSolutionDirectory().getAbsolutePath());
        assertEquals(workingDirPath + "/coverage.xml",unitTestRunner.getCoverageXmlPath());
    }
    
    

}
