package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class WindowsVsTestRunnerTest {

    private VsTestRunner runner;
    @Before
    public void before() {
        
    }
    
    @Test
    public void createTestRunner_ExpectValid() {
        createRunner();
        expectRunnerIsValid();
    }
    
    @Test
    public void simpleRunner_verifyInvocation() {
        createRunner();
        PropertiesHelper propertiesHelper = mock(PropertiesHelper.class);
        runner.setPropertiesHelper(propertiesHelper);
        VisualStudioSolution solution = mock(VisualStudioSolution.class);
        runner.setSolution(solution);
        
        String sonarWorkingDirectory="bogus";
        String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
        runner.setCoverageXmlPath(coverageXmlPath);
        runner.setSonarPath(sonarWorkingDirectory);
        runner.runTests();
    }

    private void expectRunnerIsValid() {
        assertNotNull(runner);
        assertTrue(runner instanceof WindowsVsTestRunner);
    }

    private void createRunner() {
        runner=WindowsVsTestRunner.create();
    }
}
