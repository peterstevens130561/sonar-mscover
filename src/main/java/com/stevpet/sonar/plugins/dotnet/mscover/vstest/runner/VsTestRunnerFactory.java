package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class VsTestRunnerFactory {
    private static Logger LOG = LoggerFactory.getLogger(VsTestRunnerFactory.class);
    public static VsTestRunner create() {
        return VsTestRunner.create();
    }
    
    /**
     * Create the basic unit testrunner:
     * - path to the executable
     * - solution directory
     * - coverage path
     * - log for test results
     * 
     * Only remaining thing is to set code coverage
     * @param propertiesHelper
     * @param moduleFileSystem
     * @param microsoftWindowsEnvironment - directory that holds the solution
     * @return
     */
    public static VsTestRunner createBasicTestRunnner(PropertiesHelper propertiesHelper, ModuleFileSystem moduleFileSystem,MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
            VsTestRunner unitTestRunner = VsTestRunner.create();
            unitTestRunner.setPropertiesHelper(propertiesHelper);
            VisualStudioSolution solution=microsoftWindowsEnvironment.getCurrentSolution();
            if(solution == null) {
                String msg = "No current solution";
                LOG.error(msg);
                throw new SonarException(msg);
            }
            unitTestRunner.setSolution(solution);
            
            String sonarWorkingDirectory=moduleFileSystem.workingDir().getAbsolutePath();
            String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
            unitTestRunner.setCoverageXmlPath(coverageXmlPath);
            unitTestRunner.setSonarPath(sonarWorkingDirectory);
            return unitTestRunner;
        }

}