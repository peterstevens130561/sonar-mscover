/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.nullsaver.NullBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultCoverageSaverFactory implements CoverageSaverFactory {

    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private DefaultResourceResolver resourceResolver;

    public DefaultCoverageSaverFactory(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            PathResolver pathResolver,
            FileSystem fileSystem) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.resourceResolver = new DefaultResourceResolver(pathResolver,
                fileSystem);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverFactory#createVsTestUnitTestCoverageSaver()
     */
    @Override
    public CoverageSaver createVsTestUnitTestCoverageSaver() {
        return new CoverageSaverBase(new NullBranchFileCoverageSaver(),
                new DefaultLineCoverageSaverFactory(resourceResolver).createUnitTestSaver(),
                microsoftWindowsEnvironment);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverFactory#createOpenCoverUnitTestCoverageSaver(com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment, org.sonar.api.scan.filesystem.PathResolver, org.sonar.api.batch.fs.FileSystem)
     */
    @Override
    public CoverageSaver createOpenCoverUnitTestCoverageSaver() {
        return new CoverageSaverBase(
                new DefaultBranchCoverageSaverFactory(resourceResolver).createUnitTestSaver(), 
                new DefaultLineCoverageSaverFactory(resourceResolver).createUnitTestSaver(), 
                microsoftWindowsEnvironment);

    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverFactory#createOpenCoverIntegrationTestCoverageSaver(com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment, org.sonar.api.scan.filesystem.PathResolver, org.sonar.api.batch.fs.FileSystem)
     */
    @Override
    public CoverageSaver createOpenCoverIntegrationTestCoverageSaver() {
        return new CoverageSaverBase(
                new DefaultBranchCoverageSaverFactory(resourceResolver).createIntegrationTestSaver(),
                new DefaultLineCoverageSaverFactory(resourceResolver).createIntegrationTestSaver(),
                microsoftWindowsEnvironment);
    }

    public CoverageSaver createOverallTestCoverageSaver() {
        return new CoverageSaverBase(
                new DefaultBranchCoverageSaverFactory(resourceResolver).createOverallSaver(),
                new DefaultLineCoverageSaverFactory(resourceResolver).createOverallSaver(),
                microsoftWindowsEnvironment);
    }

}
