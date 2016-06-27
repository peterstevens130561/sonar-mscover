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
