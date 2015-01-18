package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.csharpsolutionfilesystem.CSharpSolutionFileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultVsTestRunnerFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

@DependedUpon(VsTestExecutionSensor.DEPENDS)
public class VsTestExecutionSensor implements Sensor {
    
    public static final String DEPENDS="VsTestSensor";
    private static final Logger LOG = LoggerFactory
            .getLogger(VsTestExecutionSensor.class);
       
    private VsTestEnvironment vsTestEnvironment;
    private FileSystem fileSystem;
    private VsTestRunner unitTestRunner;

    private MsCoverProperties propertiesHelper;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private AbstractVsTestRunnerFactory vsTestRunnerFactory = new DefaultVsTestRunnerFactory();
    
    public VsTestExecutionSensor(VsTestEnvironment vsTestEnvironment, MsCoverProperties propertiesHelper,FileSystem fileSystem,MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.vsTestEnvironment = vsTestEnvironment;
        this.fileSystem = fileSystem;
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
        this.propertiesHelper = propertiesHelper;
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
            fileSystem = CSharpSolutionFileSystem.createFromProject(fileSystem);
        }
        
        LOG.info("MsCover : started running tests");

        runUnitTests();   
        updateTestEnvironment();
    }

    private String runUnitTests() {
        unitTestRunner = vsTestRunnerFactory.createBasicTestRunnner(propertiesHelper, fileSystem,microsoftWindowsEnvironment);
        unitTestRunner.setDoCodeCoverage(true);
        unitTestRunner.runTests();
        return unitTestRunner.getCoverageXmlPath();
    }


    
    private void updateTestEnvironment() {
        String testResultsPath=unitTestRunner.getResultsXmlPath();
        vsTestEnvironment.setTestResultsXmlPath(testResultsPath);
        
        String coverageXmlPath=unitTestRunner.getCoverageXmlPath();
        vsTestEnvironment.setCoverageXmlPath(coverageXmlPath);
        vsTestEnvironment.setTestsHaveRun();
        
        LOG.info("MsCover : running tests completed");
        LOG.info("MsCover : coverage in {}",coverageXmlPath);
        LOG.info("MsCover : results in {}",testResultsPath);
    }


    /**
     * @param vsTestRunnerFactory the vsTestRunnerFactory to set
     */
    public void setVsTestRunnerFactory(
            AbstractVsTestRunnerFactory vsTestRunnerFactory) {
        this.vsTestRunnerFactory = vsTestRunnerFactory;
    }



}
