package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;

public class OpenCoverSpecFlowTestSensor implements Sensor {

    private IntegrationTestsConfiguration integrationTestsConfiguration;

    public OpenCoverSpecFlowTestSensor(IntegrationTestsConfiguration integrationTestsConfiguration) {
        this.integrationTestsConfiguration = integrationTestsConfiguration;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
       Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
       return pattern.matcher(project.getName()).matches() && project.isModule();
    }

    @Override
    public void analyse(Project module, SensorContext context) {
        // TODO Auto-generated method stub

    }

}
