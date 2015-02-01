package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.AbstractDotNetSensor;
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
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private ResourceMediator resourceMediator;
    public OpenCoverCoverageResultsSensor(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MsCoverProperties propertiesHelper,
            VsTestEnvironment vsTestEnvironment,
            FileSystem fileSystem,
            ResourceMediator resourceMediator) {

        super(microsoftWindowsEnvironment, "OpenCover", propertiesHelper.getMode());
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.propertiesHelper = propertiesHelper;
        this.vsTestEnvironment=vsTestEnvironment;
        this.resourceMediator=resourceMediator;
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
        MeasureSaver measureSaver = SonarMeasureSaver.create(project,sensorContext, resourceMediator);
        SonarCoverageSaver sonarCoverageSaver = new SonarCoverageSaver(sensorContext, project, measureSaver);
        SonarCoverage sonarCoverageRegistry = vsTestEnvironment.getSonarCoverage();
        List<File> testSourceFiles=microsoftWindowsEnvironment.getUnitTestSourceFiles();
        sonarCoverageSaver.setCoverageRegistry(sonarCoverageRegistry);
        sonarCoverageSaver.setExcludeSourceFiles(testSourceFiles);
        sonarCoverageSaver.save();
    }

}
