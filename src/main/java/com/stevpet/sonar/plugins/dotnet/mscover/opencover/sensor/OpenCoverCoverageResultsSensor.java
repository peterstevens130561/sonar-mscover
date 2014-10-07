package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.sensor.AbstractDotNetSensor;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
@DependsUpon("OpenCoverRunningVsTest")
public class OpenCoverCoverageResultsSensor extends AbstractDotNetSensor {

    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageResultsSensor.class);
    private MsCoverProperties propertiesHelper;
    private VsTestEnvironment vsTestEnvironment;
    private TimeMachine timeMachine;

    public OpenCoverCoverageResultsSensor(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MsCoverProperties propertiesHelper,
            VsTestEnvironment vsTestEnvironment,
            TimeMachine timeMachine) {
        super(microsoftWindowsEnvironment, "OpenCover", propertiesHelper.getMode());
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
        if(isTestProject(project)) {
            return false;
        }
        if(!propertiesHelper.runOpenCover()) {
            return false;
        }

        LOG.info("Will execute " + project.getName());
        return true;
    }
    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        if(!vsTestEnvironment.getTestsHaveRun()) {
            LOG.info("Will not store OpenCover coverage data, as tests have not run");
            return ;
        }
        LOG.info("Saving opencover line & branch coverage for " + project.getName());
        ResourceMediator resourceMediator = ResourceMediator.createWithFilters(sensorContext, project, timeMachine, propertiesHelper);
        MeasureSaver measureSaver = SonarMeasureSaver.create(sensorContext, resourceMediator);
        SonarCoverageSaver sonarCoverageSaver = new SonarCoverageSaver(sensorContext, project, measureSaver);
        SonarCoverage sonarCoverageRegistry = vsTestEnvironment.getSonarCoverage();
        sonarCoverageSaver.setCoverageRegistry(sonarCoverageRegistry);
        sonarCoverageSaver.save();
    }

}
