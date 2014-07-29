package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class UnitTestRunnerFactory {
    private static Logger LOG = LoggerFactory.getLogger(UnitTestRunnerFactory.class);
    public static UnitTestRunner create() {
        return UnitTestRunner.create();
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
    public static UnitTestRunner createBasicTestRunnner(PropertiesHelper propertiesHelper, ModuleFileSystem moduleFileSystem,MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
            UnitTestRunner unitTestRunner = UnitTestRunner.create();
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
