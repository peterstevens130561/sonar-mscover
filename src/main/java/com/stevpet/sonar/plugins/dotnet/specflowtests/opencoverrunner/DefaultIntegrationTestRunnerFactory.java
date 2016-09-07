/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
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
    
