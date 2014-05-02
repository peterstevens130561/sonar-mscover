package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.dotnet.api.DotNetConstants;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
public class VsTestSensor implements Sensor {
    private static final Logger LOG = LoggerFactory
            .getLogger(VsTestSensor.class);
       
    private VsTestEnvironment vsTestEnvironment;
    private ModuleFileSystem moduleFileSystem;
    private UnitTestRunner unitTestRunner;
    
    
    public VsTestSensor(VsTestEnvironment vsTestEnvironment, Settings settings,ModuleFileSystem moduleFileSystem) {
        this.vsTestEnvironment = vsTestEnvironment;
        this.moduleFileSystem = moduleFileSystem;
        
        PropertiesHelper propertiesHelper = new PropertiesHelper(settings);
        unitTestRunner = UnitTestRunner.create();
        unitTestRunner.setPropertiesHelper(propertiesHelper);
    }


    public boolean shouldExecuteOnProject(Project project) {
        boolean shouldExecute=unitTestRunner.shouldRun() && project.isRoot();
        LOG.info("MsCover : running tests {}",shouldExecute);       
        return shouldExecute;
    }

    public void analyse(Project project, SensorContext context) {
        LOG.info("MsCover : started running tests");
        String coverageXmlPath = moduleFileSystem.workingDir()
                + "/coverage.xml";
        unitTestRunner.setCoverageXmlPath(coverageXmlPath);

        unitTestRunner.setSolutionDirectory(moduleFileSystem.baseDir());
        unitTestRunner.setSonarPath(moduleFileSystem.workingDir().getAbsolutePath());
        unitTestRunner.runTests();
        vsTestEnvironment.setResultsXmlPath(unitTestRunner.getResultsXmlPath());
        vsTestEnvironment.setCoverageXmlPath(coverageXmlPath);
        
        LOG.info("MsCover : running tests completed");
        LOG.info("MsCover : coverage in {}",coverageXmlPath);
        LOG.info("MsCover : results in {}",unitTestRunner.getResultsXmlPath());
    }

}
