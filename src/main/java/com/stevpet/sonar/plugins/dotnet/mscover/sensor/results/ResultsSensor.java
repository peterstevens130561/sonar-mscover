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
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.importer.cplusplus.CPlusPlusImporterSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.UnitTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;


@DependsUpon({CPlusPlusImporterSensor.DEPENDS,VsTestSensor.DEPENDS,DotNetConstants.CORE_PLUGIN_EXECUTED})
public class ResultsSensor implements Sensor {
    static final Logger LOG = LoggerFactory
            .getLogger(ResultsSensor.class);
    private PropertiesHelper propertiesHelper ;
    private UnitTestRunner unitTestRunner;
    private TimeMachine timeMachine;
    private VsTestEnvironment vsTestEnvironment;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

   
    
    public ResultsSensor(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,Settings settings,TimeMachine timeMachine,ModuleFileSystem moduleFileSystem,VsTestEnvironment vsTestEnvironment) {
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment ;
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
        if(propertiesHelper.getRunMode() == RunMode.SKIP) {
            return false;
        }
        boolean resultsDefined=StringUtils.isNotEmpty(resultsPath);
        boolean rightLevel = project.isRoot() == propertiesHelper.excuteRoot();
        boolean shouldRunUnitTests=unitTestRunner.shouldRun();
        boolean shouldExecute = (resultsDefined || shouldRunUnitTests) && rightLevel;
        LOG.info("ResultsSensor {}",shouldExecute);
        return shouldExecute;
    }

    public void analyse(Project project, SensorContext sensorContext) {
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
        
        ResourceMediator resourceMediator = ResourceMediator.createWithFilters(sensorContext,project,timeMachine,propertiesHelper);            
        MeasureSaver measureSaver = SonarMeasureSaver.create(sensorContext,resourceMediator);

        UnitTestAnalyser analyser = new UnitTestAnalyser(project,sensorContext,measureSaver);
        analyser.analyseResults(coveragePath, resultsPath);
        if(unitTestRunner.shouldRun()) {
            CoverageHelper coverageHelper = CoverageHelper.create(propertiesHelper,microsoftWindowsEnvironment);
            coverageHelper.injectUnitSavers(measureSaver);
            coverageHelper.analyse(project,coveragePath);
        }
    }

    

}
