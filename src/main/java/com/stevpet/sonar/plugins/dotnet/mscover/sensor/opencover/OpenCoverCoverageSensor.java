package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.sensor.AbstractDotNetSensor;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class OpenCoverCoverageSensor extends AbstractDotNetSensor {

    private PropertiesHelper propertiesHelper;

    public OpenCoverCoverageSensor(
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
    public void analyse(Project project, SensorContext context) {
        if(isTestProject(project)) {
            return;
        }
        if(!shouldExecuteOnProject(project)) {
            return;
        }
    }

}
