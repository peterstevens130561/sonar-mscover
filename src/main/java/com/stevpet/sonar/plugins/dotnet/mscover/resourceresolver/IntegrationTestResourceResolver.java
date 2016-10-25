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
package com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver;

import java.io.File;
import java.io.IOException;

import org.sonar.api.batch.fs.FileSystem;


import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;

/**
 * Integration tests are executed in other workspaces, therefore the files do not simply map
 * into the local workspace i.e. a file may in the current workspace be in
 * C:/Development/Radiant/Main/JewelEarth/Core/GoodStuff/Views/BasicView.cs
 * and the integration test may be
 * F:53,44/Tests/JewelEarth/Core/GoodStuff/Views/BasicView.cs
 * 
 * This is resolved by a configuration item, the WorkSpaceRoot
 * @author stevpet
 *
 */
public class IntegrationTestResourceResolver implements ResourceResolver {

    private FileSystem fileSystem;
    private MsCoverConfiguration msCoverConfiguration;

    public IntegrationTestResourceResolver(FileSystem fileSystem, MsCoverConfiguration msCoverConfiguration) {
        this.fileSystem = fileSystem;
        this.msCoverConfiguration = msCoverConfiguration;

    }

    @Override
    public org.sonar.api.resources.File getFile(File file) {
        File baseDir = fileSystem.baseDir();
        File rootDir = msCoverConfiguration.getWorkSpaceRoot();
        String solutionPath = getPathFromRootToSolution(baseDir, rootDir);
        String resolvePath = getCanonicalPath(file);
        int firstIndex = resolvePath.indexOf(solutionPath);
        if (firstIndex == -1) {
            return null; 
        }
        int endOfSolutionPath = firstIndex + solutionPath.length();

        String relativePath = resolvePath.substring(endOfSolutionPath);
        org.sonar.api.resources.File sonarFile = org.sonar.api.resources.File.create(relativePath);
        return sonarFile;
    }

    private String getPathFromRootToSolution(File solutionDir, File rootDir) {
        String rootPath = null;
        String solutionPath = null;
        try {
            rootPath = rootDir.getCanonicalPath();
            solutionPath = solutionDir.getCanonicalPath();
        } catch (IOException e) {
            String msg = "Could not get canonical path " + e.getLocalizedMessage();
            throw new IllegalStateException(msg, e);
        }
        if (!solutionPath.startsWith(rootPath)) {
            String msg = "Project=" + solutionPath + " not found in root= " + rootPath;
            throw new IllegalStateException(msg);
        }
        int len = rootPath.length();
        String pathToSolution = solutionPath.substring(len + 1);
        return pathToSolution;
    }

    private String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            String msg = "Could not get CanonicalPath for " + file.getAbsolutePath();
            throw new IllegalStateException(msg);
        }
    }
}
