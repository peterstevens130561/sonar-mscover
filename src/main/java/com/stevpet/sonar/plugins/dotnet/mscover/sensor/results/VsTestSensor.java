package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.csharpsolutionfilesystem.CSharpSolutionFileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

@DependedUpon(VsTestSensor.DEPENDS)
public class VsTestSensor implements Sensor {
    
    public static final String DEPENDS="VsTestSensor";
    private static final Logger LOG = LoggerFactory
            .getLogger(VsTestSensor.class);
       
    private VsTestEnvironment vsTestEnvironment;
    private ModuleFileSystem moduleFileSystem;
    private UnitTestRunner unitTestRunner;

    private PropertiesHelper propertiesHelper;
    
    
    public VsTestSensor(VsTestEnvironment vsTestEnvironment, Settings settings,ModuleFileSystem moduleFileSystem) {
        this.vsTestEnvironment = vsTestEnvironment;
        this.moduleFileSystem = moduleFileSystem;
        
        propertiesHelper = PropertiesHelper.create(settings);
        unitTestRunner = UnitTestRunner.create();
        unitTestRunner.setPropertiesHelper(propertiesHelper);
    }


    public boolean shouldExecuteOnProject(Project project) {
        RunMode runMode=propertiesHelper.getRunMode() ;
        boolean shouldExecute=runMode == RunMode.RUNVSTEST;
        return shouldExecute;
 
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
        File baseDir=moduleFileSystem.baseDir();
        unitTestRunner.setSolutionDirectory(baseDir);
        
        String sonarWorkingDirectory=moduleFileSystem.workingDir().getAbsolutePath();
        String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
        unitTestRunner.setCoverageXmlPath(coverageXmlPath);
        unitTestRunner.setSonarPath(sonarWorkingDirectory);
        unitTestRunner.runTests();
        return coverageXmlPath;
    }
    
    private void updateTestEnvironment(String coverageXmlPath) {
        String testResultsPath=unitTestRunner.getResultsXmlPath();
        vsTestEnvironment.setResultsXmlPath(testResultsPath);
        vsTestEnvironment.setCoverageXmlPath(coverageXmlPath);
        vsTestEnvironment.setTestsHaveRun();
        
        LOG.info("MsCover : running tests completed");
        LOG.info("MsCover : coverage in {}",coverageXmlPath);
        LOG.info("MsCover : results in {}",testResultsPath);
    }


}
