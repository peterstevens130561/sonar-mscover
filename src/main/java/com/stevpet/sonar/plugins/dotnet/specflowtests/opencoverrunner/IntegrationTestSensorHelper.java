package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.regex.Pattern;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestSensorHelper {

    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    public IntegrationTestSensorHelper(MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }

    public boolean isSolutionWithIntegrationTestProjects(Project project,Pattern pattern) {
        return project.isModule() && hasTestProjects(pattern) ;
    }

    private boolean hasTestProjects(Pattern pattern) {
        if(pattern==null) {
            return false;
        }
        return microsoftWindowsEnvironment.hasTestProjects(pattern);
    }

}
