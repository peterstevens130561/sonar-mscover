package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public class DefaultVsTestRunnerFactory implements AbstractVsTestRunnerFactory {
    private static Logger LOG = LoggerFactory.getLogger(DefaultVsTestRunnerFactory.class);
    
    
    public VsTestRunner create() {
        return WindowsVsTestRunner.create();
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
    public VsTestRunner createBasicTestRunnner(MsCoverProperties propertiesHelper, FileSystem fileSystem,MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
            VsTestRunner unitTestRunner = WindowsVsTestRunner.create();
            unitTestRunner.setPropertiesHelper(propertiesHelper);
            VisualStudioSolution solution=microsoftWindowsEnvironment.getCurrentSolution();
            if(solution == null) {
                String msg = "No current solution";
                LOG.error(msg);
                throw new SonarException(msg);
            }
            unitTestRunner.setSolution(solution);
            
            String sonarWorkingDirectory=fileSystem.workDir().getAbsolutePath();
            String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
            unitTestRunner.setCoverageXmlPath(coverageXmlPath);
            unitTestRunner.setSonarPath(sonarWorkingDirectory);
            return unitTestRunner;
        }

}
