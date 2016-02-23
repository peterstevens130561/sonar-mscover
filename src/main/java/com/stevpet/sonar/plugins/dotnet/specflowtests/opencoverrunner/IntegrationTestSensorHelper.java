package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.regex.Pattern;

import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestSensorHelper {

    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private IntegrationTestsConfiguration integrationTestsConfiguration;
    public IntegrationTestSensorHelper(MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }

    public IntegrationTestSensorHelper(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            IntegrationTestsConfiguration integrationTestsConfiguration) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.integrationTestsConfiguration=integrationTestsConfiguration;
    }

    public boolean isSolutionWithIntegrationTestProjects(Project project) {
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        return project.isModule() && hasTestProjects(pattern) ;
    }

    private boolean hasTestProjects(Pattern pattern) {
        if(pattern==null) {
            return false;
        }
        return microsoftWindowsEnvironment.hasTestProjects(pattern);
    }

}
