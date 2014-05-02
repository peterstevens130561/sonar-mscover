package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.dotnet.api.DotNetConstants;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.importer.cplusplus.CPlusPlusImporterSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageAnalyser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;


@DependsUpon({CPlusPlusImporterSensor.DEPENDS,DotNetConstants.CORE_PLUGIN_EXECUTED})
public class ResultsSensor implements Sensor {
    static final Logger LOG = LoggerFactory
            .getLogger(ResultsSensor.class);
    private PropertiesHelper propertiesHelper ;
    private UnitTestRunner unitTestRunner;
    private TimeMachine timeMachine;
    private VsTestEnvironment vsTestEnvironment;
   
    
    public ResultsSensor(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,Settings settings,TimeMachine timeMachine,ModuleFileSystem moduleFileSystem,VsTestEnvironment vsTestEnvironment) {
        this.timeMachine = timeMachine;
        propertiesHelper = PropertiesHelper.create(settings);
        unitTestRunner = UnitTestRunner.create();
        unitTestRunner.setPropertiesHelper(propertiesHelper);
        this.vsTestEnvironment = vsTestEnvironment;
    }
    
    /**
     * tests whether the sensor should execute on the project
     */
    public boolean shouldExecuteOnProject(Project project) {
        String resultsPath=propertiesHelper.getUnitTestResultsPath();
        boolean shouldExecute = (StringUtils.isNotEmpty(resultsPath) || unitTestRunner.shouldRun()) && (project.isRoot() == propertiesHelper.excuteRoot());
        LOG.info("ResultsSensor {}",shouldExecute);
        return shouldExecute;
    }

    public void analyse(Project project, SensorContext context) {
        LOG.info("MsCover Starting analysing test results");
        String coveragePath;
        String resultsPath;
        if(unitTestRunner.shouldRun()) {
            resultsPath=vsTestEnvironment.getXmlResultsPath();
            coveragePath=vsTestEnvironment.getXmlCoveragePath();
        } else {
            LOG.info("MsCover using test results");
            coveragePath = propertiesHelper.getUnitTestCoveragePath();
            resultsPath=propertiesHelper.getUnitTestResultsPath();
        }
        UnitTestAnalyser analyser = new UnitTestAnalyser(project,context);
        analyser.analyseResults(coveragePath, resultsPath);
        if(unitTestRunner.shouldRun()) {
            CoverageAnalyser coverageAnalyser = new CoverageAnalyser(project,context, timeMachine, propertiesHelper);
            coverageAnalyser.analyseResults(coveragePath);
        }
    }

    

}
