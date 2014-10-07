package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.DotNetConstants;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.importer.cplusplus.CPlusPlusImporterSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.AbstractCoverageHelperFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.SonarCoverageHelperFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;


@DependsUpon({CPlusPlusImporterSensor.DEPENDS,VsTestExecutionSensor.DEPENDS,DotNetConstants.CORE_PLUGIN_EXECUTED})
public class VsTestUnitTestResultsSensor implements Sensor {
    static final Logger LOG = LoggerFactory
            .getLogger(VsTestUnitTestResultsSensor.class);
    private MsCoverProperties propertiesHelper ;
    private VsTestRunner unitTestRunner;
    private TimeMachine timeMachine;
    private VsTestEnvironment vsTestEnvironment;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

   
    
    public VsTestUnitTestResultsSensor(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MsCoverProperties propertiesHelper,TimeMachine timeMachine,
            VsTestEnvironment vsTestEnvironment) {
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment ;
        this.timeMachine = timeMachine;
        this.propertiesHelper = propertiesHelper;
        unitTestRunner = VsTestRunner.create();
        unitTestRunner.setPropertiesHelper(propertiesHelper);
        this.vsTestEnvironment = vsTestEnvironment;
    }
    
    /**
     * tests whether the sensor should execute on the project
     */
    public boolean shouldExecuteOnProject(Project project) {

        if(propertiesHelper.getRunMode() == RunMode.SKIP) {
            return false;
        }
        String resultsPath=propertiesHelper.getUnitTestResultsPath();
        boolean resultsDefined=StringUtils.isNotEmpty(resultsPath);
        
        boolean rightLevel = project.isRoot() == propertiesHelper.excuteRoot();
        boolean shouldRunUnitTests=propertiesHelper.runVsTest();
        boolean shouldExecute = (resultsDefined || shouldRunUnitTests) && rightLevel;
        LOG.info("ResultsSensor {}",shouldExecute);
        return shouldExecute;
    }

    public void analyse(Project project, SensorContext sensorContext) {
        LOG.info("MsCover Starting analysing test results");
        String coveragePath;
        String resultsPath;
        ResourceMediator resourceMediator = ResourceMediator.createWithFilters(sensorContext,project,timeMachine,propertiesHelper);            
        MeasureSaver measureSaver = SonarMeasureSaver.create(sensorContext,resourceMediator);
        SourceFilePathHelper sourcePathHelper = new SourceFilePathHelper();
        VsTestUnitTestResultsAnalyser unitTestAnalyser = new VsTestUnitTestResultsAnalyser(project,measureSaver,sourcePathHelper,resourceMediator);     
        if(propertiesHelper.runVsTest()) {
            coveragePath=vsTestEnvironment.getXmlCoveragePath();
            resultsPath=vsTestEnvironment.getXmlResultsPath();
        } else {
            LOG.info("MsCover using test results");
            coveragePath = propertiesHelper.getUnitTestCoveragePath();
            resultsPath=propertiesHelper.getUnitTestResultsPath();
        }
        unitTestAnalyser.analyseVsTestResults(coveragePath, resultsPath);
        if(propertiesHelper.runVsTest()) {
            AbstractCoverageHelperFactory coverageHelperFactory = new SonarCoverageHelperFactory();
            CoverageHelper coverageHelper = coverageHelperFactory.createUnitTestCoverageHelper(propertiesHelper, microsoftWindowsEnvironment, measureSaver);
            coverageHelper.analyse(project,coveragePath);
        }
    }

    

}
