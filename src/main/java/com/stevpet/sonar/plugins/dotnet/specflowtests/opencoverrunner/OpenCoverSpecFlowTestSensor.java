package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Mode;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Tool;

public class OpenCoverSpecFlowTestSensor implements Sensor {

    private IntegrationTestsConfiguration integrationTestsConfiguration;
    private IntegrationTestRunnerApplication integrationTestRunnerApplication;
    private OpenCoverSpecFlowTestSaverSensor saverSensor ; 

    public OpenCoverSpecFlowTestSensor(IntegrationTestsConfiguration integrationTestsConfiguration, IntegrationTestRunnerApplication integrationTestRunnerApplication, OpenCoverSpecFlowTestSaverSensor saverSensor) {
        this.integrationTestsConfiguration = integrationTestsConfiguration;
        this.integrationTestRunnerApplication = integrationTestRunnerApplication;
        this.saverSensor = saverSensor;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return project.isModule() && integrationTestsConfiguration.matches(Tool.OPENCOVER, Mode.ACTIVE) ;
    }

    @Override
    public void analyse(Project module, SensorContext context) {
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        boolean isTestProject=pattern.matcher(module.getName()).matches();       // TODO Auto-generated method stub
        integrationTestRunnerApplication.execute();
        if(saverSensor.shouldExecuteOnProject(module)) {
            saverSensor.analyse(module, context);
        }
        
    }

}
