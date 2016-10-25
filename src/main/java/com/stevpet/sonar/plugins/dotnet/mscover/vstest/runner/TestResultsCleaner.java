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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.exceptions.MsCoverInvalidSonarWorkingDir;

public class TestResultsCleaner {
    
    private FileSystem fileSystem;
    
    public TestResultsCleaner(FileSystem fileSystem) {
        this.fileSystem=fileSystem;
    }
    
    public void execute() {
        File sonarDir = fileSystem.workDir();
        if (sonarDir == null) {
            throw new IllegalStateException("sonarPath not set");
        }
        String sonarPath=sonarDir.getAbsolutePath();
        if (!".sonar".equalsIgnoreCase(sonarDir.getName())) {
            throw new MsCoverInvalidSonarWorkingDir(sonarPath);
        }
        File testResultsDir = new File(sonarPath, "TestResults");
        if (testResultsDir.exists()) {
            FileUtils.deleteQuietly(testResultsDir);
        }
    
    }

}
