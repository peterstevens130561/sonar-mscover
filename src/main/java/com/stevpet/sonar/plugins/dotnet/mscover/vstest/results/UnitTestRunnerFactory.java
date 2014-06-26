package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;

import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class UnitTestRunnerFactory {
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
     * @return
     */
    public static UnitTestRunner createBasicTestRunnner(PropertiesHelper propertiesHelper, ModuleFileSystem moduleFileSystem) {
            UnitTestRunner unitTestRunner = UnitTestRunner.create();
            unitTestRunner.setPropertiesHelper(propertiesHelper);
            
            File baseDir=moduleFileSystem.baseDir();
            unitTestRunner.setSolutionDirectory(baseDir);
            
            String sonarWorkingDirectory=moduleFileSystem.workingDir().getAbsolutePath();
            String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
            unitTestRunner.setCoverageXmlPath(coverageXmlPath);
            unitTestRunner.setSonarPath(sonarWorkingDirectory);
            return unitTestRunner;
        }

}
