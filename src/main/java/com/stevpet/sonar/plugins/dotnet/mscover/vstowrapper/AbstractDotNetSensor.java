package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper;

import org.sonar.api.batch.Sensor;
import org.sonar.api.resources.Project;

public abstract class AbstractDotNetSensor implements Sensor {

    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    public AbstractDotNetSensor(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            String plugin, String mode) {
        this.microsoftWindowsEnvironment  = microsoftWindowsEnvironment;
    }


    public abstract String[] getSupportedLanguages();

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return project.isRoot();
    }
    
    protected MicrosoftWindowsEnvironment getMicrosoftWindowsEnvironment() {
        return microsoftWindowsEnvironment;
    }
    
    protected boolean isTestProject(Project project) {
        return false;
    }

}
