/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public class DefaultVsTestRunnerFactory implements AbstractVsTestRunnerFactory {
    private static Logger LOG = LoggerFactory.getLogger(DefaultVsTestRunnerFactory.class);
    
    
    public VsTestRunner create() {
        return WindowsVsTestRunner.create();
    }
    
    /**
     * Create the basic unit testrunner:
     * - path to the executable
     * - solution directory
     * - coverage path
     * - log for test results
     * 
     * Only remaining thing is to set code coverage
     * @param propertiesHelper
     * @param moduleFileSystem
     * @param microsoftWindowsEnvironment - directory that holds the solution
     * @return
     * @deprecated use {@link #createBasicTestRunnner(MsCoverProperties, ModuleFileSystem)
     */
    public VsTestRunner createBasicTestRunnner(MsCoverProperties propertiesHelper, ModuleFileSystem moduleFileSystem,MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
            VsTestRunner unitTestRunner = WindowsVsTestRunner.create();
            unitTestRunner.setPropertiesHelper(propertiesHelper);
            VisualStudioSolution solution=microsoftWindowsEnvironment.getCurrentSolution();
            if(solution == null) {
                String msg = "No current solution";
                LOG.error(msg);
                throw new SonarException(msg);
            }
            unitTestRunner.setSolution(solution);
            
            String sonarWorkingDirectory=moduleFileSystem.workingDir().getAbsolutePath();
            String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
            unitTestRunner.setCoverageXmlPath(coverageXmlPath);
            unitTestRunner.setSonarPath(sonarWorkingDirectory);
            return unitTestRunner;
        }

    @Override
    public VsTestRunner createBasicTestRunnner(
            MsCoverProperties propertiesHelper,
            ModuleFileSystem moduleFileSystem) {
        File startDir=moduleFileSystem.baseDir();
        VsTestRunner unitTestRunner = WindowsVsTestRunner.create();
        unitTestRunner.setPropertiesHelper(propertiesHelper);
        unitTestRunner.setStartDir(startDir);
        
        String sonarWorkingDirectory=moduleFileSystem.workingDir().getAbsolutePath();
        String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
        unitTestRunner.setCoverageXmlPath(coverageXmlPath);
        unitTestRunner.setSonarPath(sonarWorkingDirectory);
        return unitTestRunner;
    }

}
