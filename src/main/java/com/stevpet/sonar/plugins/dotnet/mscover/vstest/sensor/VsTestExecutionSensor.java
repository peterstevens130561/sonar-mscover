package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.csharpsolutionfilesystem.CSharpSolutionFileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerFactory;

@DependedUpon(VsTestExecutionSensor.DEPENDS)
public class VsTestExecutionSensor implements Sensor {
    
    public static final String DEPENDS="VsTestSensor";
    private static final Logger LOG = LoggerFactory
            .getLogger(VsTestExecutionSensor.class);
       
    private VsTestEnvironment vsTestEnvironment;
    private ModuleFileSystem moduleFileSystem;
    private VsTestRunner unitTestRunner;

    private PropertiesHelper propertiesHelper;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    
    
    public VsTestExecutionSensor(VsTestEnvironment vsTestEnvironment, Settings settings,ModuleFileSystem moduleFileSystem,MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.vsTestEnvironment = vsTestEnvironment;
        this.moduleFileSystem = moduleFileSystem;
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
        propertiesHelper = PropertiesHelper.create(settings);

    }


    public boolean shouldExecuteOnProject(Project project) {
        return propertiesHelper.runVsTest();
    }

    public void analyse(Project project, SensorContext context) {

        if(vsTestEnvironment.getTestsHaveRun()) {
            LOG.info("MsCover : tests have run already");
            return;
        }
        if(!project.isRoot()) {
            LOG.info("MsCover : CSharp solution, using first project to run tests");
            moduleFileSystem = CSharpSolutionFileSystem.createFromProject(moduleFileSystem);
        }
        
        LOG.info("MsCover : started running tests");

        String coverageXmlPath = runUnitTests();   
        updateTestEnvironment(coverageXmlPath);
    }

    private String runUnitTests() {
        unitTestRunner = VsTestRunnerFactory.createBasicTestRunnner(propertiesHelper, moduleFileSystem,microsoftWindowsEnvironment);
        unitTestRunner.setDoCodeCoverage(true);
        unitTestRunner.runTests();
        return unitTestRunner.getCoverageXmlPath();
    }


    
    private void updateTestEnvironment(String coverageXmlPath) {
        String testResultsPath=unitTestRunner.getResultsXmlPath();
        vsTestEnvironment.setTestResultsXmlPath(testResultsPath);
        vsTestEnvironment.setCoverageXmlPath(coverageXmlPath);
        vsTestEnvironment.setTestsHaveRun();
        
        LOG.info("MsCover : running tests completed");
        LOG.info("MsCover : coverage in {}",coverageXmlPath);
        LOG.info("MsCover : results in {}",testResultsPath);
    }


}