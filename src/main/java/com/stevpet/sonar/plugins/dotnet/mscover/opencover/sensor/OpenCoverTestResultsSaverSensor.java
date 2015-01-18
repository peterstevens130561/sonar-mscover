package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.AbstractDotNetSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.DefaultResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsAnalyser;
@DependsUpon("OpenCoverRunningVsTest")
public class OpenCoverTestResultsSaverSensor extends AbstractDotNetSensor {

    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageResultsSensor.class);
    private MsCoverProperties propertiesHelper;
    private VsTestEnvironment vsTestEnvironment;
    private TimeMachine timeMachine;
    private ResourceMediatorFactory resourceMediatorFactory = new DefaultResourceMediatorFactory();
    private VsTestUnitTestResultsAnalyser analyser = new VsTestUnitTestResultsAnalyser();
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    public OpenCoverTestResultsSaverSensor(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MsCoverProperties propertiesHelper,
            VsTestEnvironment vsTestEnvironment,
            TimeMachine timeMachine) {
        super(microsoftWindowsEnvironment, "OpenCover", propertiesHelper.getMode());
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.propertiesHelper = propertiesHelper;
        this.vsTestEnvironment=vsTestEnvironment;
        this.timeMachine = timeMachine;
    }

    @Override
    public String[] getSupportedLanguages() {
        return new String[] {"cs"};
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        if(!super.shouldExecuteOnProject(project)) {
            return false;
        }
        if(!propertiesHelper.runOpenCover()) {
            return false;
        }

        LOG.info("Will execute OpenCoverTestResultsSaverSensor for" + project.getName());
        return true;
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        if(!vsTestEnvironment.getTestsHaveRun()) {
            LOG.info("Will not execute OpenCoverage test results sensor, as tests have not run");
            return;
        }
        LOG.info("Saving test results of " + project.getName()  );
        ResourceMediator resourceMediator = resourceMediatorFactory.createWithFilters(sensorContext,project,timeMachine,propertiesHelper);            
        
        MeasureSaver measureSaver = SonarMeasureSaver.create(sensorContext,resourceMediator);
        analyser.setProject(project);
        analyser.setMeasureSaver(measureSaver);
        analyser.setResourceMediator(resourceMediator) ;
        
        VisualStudioSolution visualStudioSolution=microsoftWindowsEnvironment.getCurrentSolution();      
        String coveragePath = vsTestEnvironment.getXmlCoveragePath();
        String resultsPath = vsTestEnvironment.getXmlResultsPath();
        analyser.analyseOpenCoverTestResults(coveragePath, resultsPath);
    }
}
