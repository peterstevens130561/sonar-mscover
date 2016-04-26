package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Mode;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Tool;

/**
 * AS a developer I want to see analysis for solutions where the SpecFlow tests are part of the
 * solution itself. 
 * -tests are run first
 * -
 * @author stevpet
 *
 */
@DependedUpon(value="IntegrationTestCoverageSaved")
public class OpenCoverSpecFlowTestSensor implements Sensor {

    private IntegrationTestsConfiguration integrationTestsConfiguration;
    private OpenCoverSpecFlowTestSaverSensor saverSensor ;
    private OpenCoverSpecFlowTestRunnerSensor runnerSensor; 

    public OpenCoverSpecFlowTestSensor(IntegrationTestsConfiguration integrationTestsConfiguration, OpenCoverSpecFlowTestRunnerSensor runnerSensor, OpenCoverSpecFlowTestSaverSensor saverSensor) {
        this.integrationTestsConfiguration = integrationTestsConfiguration;
        this.runnerSensor = runnerSensor;
        this.saverSensor = saverSensor;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return project.isModule() && integrationTestsConfiguration.matches(Tool.OPENCOVER, Mode.ACTIVE) ;
    }

    @Override
    public void analyse(Project module, SensorContext context) {
        runnerSensor.analyse(module, context);
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        boolean isTestProject=pattern.matcher(module.getName()).matches();       // TODO Auto-generated method stub

        if(!isTestProject) {
            saverSensor.analyse(module, context);
        }
        
    }

}
