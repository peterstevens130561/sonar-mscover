package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import org.sonar.api.batch.fs.FileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpecFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultIntegrationTestRunnerFactory implements IntegrationTestRunnerFactory {
    private MsCoverConfiguration msCoverConfiguration;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;

    public DefaultIntegrationTestRunnerFactory(
            MsCoverConfiguration msCoverConfiguration,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            FileSystem fileSystem) {
        this.msCoverConfiguration = msCoverConfiguration;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.fileSystem = fileSystem;
    }
        /* (non-Javadoc)
         * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.IntegrationTestRunnerFactory#create()
         */
        @Override
        public IntegrationTestRunner create() {
            return new SpecflowIntegrationTestRunner(
                    new OpenCoverModuleSaver(), DefaultOpenCoverTestRunner.create(
                            msCoverConfiguration, microsoftWindowsEnvironment,
                            fileSystem),
                    SpecFlowTestResultsBuilder.create(microsoftWindowsEnvironment));
        }
    }
    
