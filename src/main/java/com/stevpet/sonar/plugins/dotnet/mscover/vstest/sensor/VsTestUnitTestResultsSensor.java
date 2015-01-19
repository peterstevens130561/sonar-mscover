package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.DotNetConstants;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.DefaultResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.AbstractCoverageHelperFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.SonarCoverageHelperFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;


@DependsUpon({VsTestExecutionSensor.DEPENDS,DotNetConstants.CORE_PLUGIN_EXECUTED})
public class VsTestUnitTestResultsSensor implements Sensor {;
    static final Logger LOG = LoggerFactory
            .getLogger(VsTestUnitTestResultsSensor.class);
    private MsCoverProperties propertiesHelper ;
    private VsTestRunner unitTestRunner;
    private TimeMachine timeMachine;
    private VsTestEnvironment vsTestEnvironment;
    private ResourceMediatorFactory resourceMediatorFactory = new DefaultResourceMediatorFactory();
    private VsTestUnitTestResultsAnalyser vsTestUnitTestResultsAnalyser = new VsTestUnitTestResultsAnalyser();
    private FileSystem fileSystem;
    
    public VsTestUnitTestResultsSensor( MsCoverProperties propertiesHelper,TimeMachine timeMachine,
            VsTestEnvironment vsTestEnvironment,
            FileSystem fileSystem) {
        this.timeMachine = timeMachine;
        this.propertiesHelper = propertiesHelper;
        unitTestRunner = WindowsVsTestRunner.create();
        unitTestRunner.setPropertiesHelper(propertiesHelper);
        this.vsTestEnvironment = vsTestEnvironment;
        this.fileSystem = fileSystem;
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
        ResourceMediator resourceMediator = resourceMediatorFactory.createWithFilters(sensorContext,project,timeMachine,propertiesHelper,fileSystem);            
        
        MeasureSaver measureSaver = SonarMeasureSaver.create(sensorContext,resourceMediator);

        vsTestUnitTestResultsAnalyser.setProject(project);
        vsTestUnitTestResultsAnalyser.setMeasureSaver(measureSaver);
        vsTestUnitTestResultsAnalyser.setResourceMediator(resourceMediator) ;
        
        if(propertiesHelper.runVsTest()) {
            coveragePath=vsTestEnvironment.getXmlCoveragePath();
            resultsPath=vsTestEnvironment.getXmlResultsPath();
        } else {
            LOG.info("MsCover using test results");
            coveragePath = propertiesHelper.getUnitTestCoveragePath();
            resultsPath=propertiesHelper.getUnitTestResultsPath();
        }
        vsTestUnitTestResultsAnalyser.analyseVsTestResults(coveragePath, resultsPath);
        if(propertiesHelper.runVsTest()) {
            AbstractCoverageHelperFactory coverageHelperFactory = new SonarCoverageHelperFactory();
            CoverageSaver coverageHelper = coverageHelperFactory.createUnitTestCoverageHelper(fileSystem, measureSaver);
            coverageHelper.analyse(project,coveragePath);
        }
    }

    public void setResourceMediatorFactory(ResourceMediatorFactory resourceMediatorFactory) {
        this.resourceMediatorFactory=resourceMediatorFactory;
    }

    public void setVsTestUnitTestResultsAnalyser(
            VsTestUnitTestResultsAnalyser mock) {
        this.vsTestUnitTestResultsAnalyser = mock;
    }

    

}
