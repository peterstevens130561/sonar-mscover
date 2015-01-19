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
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(UnitTestRunner.class)
public class UnitTestRunnerFactoryTest {
    
    private FileSystem fileSystem;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private MsCoverProperties propertiesHelper;
    private Settings settings;

    @Before()
    public void before() {
        fileSystem=mock(FileSystem.class);
        microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);   
        propertiesHelper=PropertiesHelper.create(settings);
    }

    
    @Test
    public void noSolutionDefined_ExpectSonarException() {
        String workingDirPath="C:/Development/Rubbish/Solution/.sonar";
        File sonarWorkingDir=new File(workingDirPath);
        when(fileSystem.workDir()).thenReturn(sonarWorkingDir);
        try {
        VsTestRunner unitTestRunner=new DefaultVsTestRunnerFactory().createBasicTestRunnner(propertiesHelper, fileSystem, microsoftWindowsEnvironment);
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
        
        when(fileSystem.workDir()).thenReturn(sonarWorkingDir);
        
        VisualStudioSolution solution=mock(VisualStudioSolution.class);
        when(solution.getSolutionDir()).thenReturn(solutionDir);
        when(microsoftWindowsEnvironment.getCurrentSolution()).thenReturn(solution);
        VsTestRunner unitTestRunner=new DefaultVsTestRunnerFactory().createBasicTestRunnner(propertiesHelper, fileSystem, microsoftWindowsEnvironment);
        assertEquals(workingDirPath,unitTestRunner.getSonarPath());
        assertEquals(solutionDir.getAbsolutePath(),unitTestRunner.getSolutionDirectory().getAbsolutePath());
        assertEquals(workingDirPath + "/coverage.xml",unitTestRunner.getCoverageXmlPath());
    }
    
    

}
