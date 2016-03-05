package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpecFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.IntegrationTestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultIntegrationTestRunnerFactory implements IntegrationTestRunnerFactory {
    private MsCoverConfiguration msCoverConfiguration;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;
    private VsTestEnvironment vsTestEnvironment;
    private Settings settings;

    public DefaultIntegrationTestRunnerFactory(
            MsCoverConfiguration msCoverConfiguration,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            FileSystem fileSystem, VsTestEnvironment vsTestEnvironment,Settings settings) {
        this.msCoverConfiguration = msCoverConfiguration;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.fileSystem = fileSystem;
        this.vsTestEnvironment = vsTestEnvironment;
        this.settings = settings;
    }
        /* (non-Javadoc)
         * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.IntegrationTestRunnerFactory#create()
         */
        @Override
        public IntegrationTestRunner create() {
            return new SpecflowIntegrationTestRunner(
                    new OpenCoverModuleSaver(), DefaultOpenCoverTestRunner.create(
                            msCoverConfiguration, microsoftWindowsEnvironment,
                            fileSystem, vsTestEnvironment),
                    SpecFlowTestResultsBuilder.create(microsoftWindowsEnvironment));
        }
    }
    
