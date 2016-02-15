package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.IntegrationTestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.DefaultMicrosoftWindowsEnvironment;

public class OpenCoverSpecFlowPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        List exported=Arrays.asList(
        		VsTestEnvironment.class,
        		DefaultMicrosoftWindowsEnvironment.class,
        		DefaultMsCoverConfiguration.class,
        		IntegrationTestCache.class,
                OpenCoverSpecFlowTestRunnerSensor.class,
                OpenCoverSpecFlowTestSaverSensor.class
                );
   
        return exported;
    }

}
