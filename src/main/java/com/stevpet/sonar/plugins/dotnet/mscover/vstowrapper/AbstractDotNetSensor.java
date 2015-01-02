package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

public abstract class AbstractDotNetSensor {

    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    public AbstractDotNetSensor(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            String plugin, String mode) {
        this.microsoftWindowsEnvironment  = microsoftWindowsEnvironment;
    }

    public void analyse(Project project, SensorContext context) {
        // TODO Auto-generated method stub
        
    }

    public abstract String[] getSupportedLanguages();

    public boolean shouldExecuteOnProject(Project project) {
        return false;
    }
    
    protected MicrosoftWindowsEnvironment getMicrosoftWindowsEnvironment() {
        return microsoftWindowsEnvironment;
    }
    
    protected boolean isTestProject(Project project) {
        return false;
    }

}
