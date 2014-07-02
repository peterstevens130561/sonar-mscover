package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.sensor.AbstractDotNetSensor;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class OpenCoverCoverageResultsSensor extends AbstractDotNetSensor {

    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageResultsSensor.class);
    private PropertiesHelper propertiesHelper;

    public OpenCoverCoverageResultsSensor(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            Settings settings) {
        super(microsoftWindowsEnvironment, "OpenCover", settings.getString(PropertiesHelper.MSCOVER_MODE));
        propertiesHelper = PropertiesHelper.create(settings);
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
    public void analyse(Project project, SensorContext context) {

    }

}
