package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.CoverageHashes;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSplitter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpecFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultIntegrationTestRunnerFactory implements IntegrationTestRunnerFactory {
    private final MsCoverConfiguration msCoverConfiguration;
    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private final FileSystem fileSystem;
    private final Settings settings;
    private final CoverageHashes coverageHashes;

    public DefaultIntegrationTestRunnerFactory(
            MsCoverConfiguration msCoverConfiguration, Settings settings,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            FileSystem fileSystem
            ) {
        this.msCoverConfiguration = msCoverConfiguration;
        this.settings = settings;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.fileSystem = fileSystem;
        this.coverageHashes = new CoverageHashes();
    }
        /* (non-Javadoc)
         * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.IntegrationTestRunnerFactory#create()
         */
        @Override
        public IntegrationTestRunner create() {
            return new SpecflowIntegrationTestRunner(
                    new OpenCoverModuleSplitter(coverageHashes), DefaultOpenCoverTestRunner.create(
                            msCoverConfiguration, settings, microsoftWindowsEnvironment,
                            fileSystem),
                    SpecFlowTestResultsBuilder.create(microsoftWindowsEnvironment));
        }
    }
    
