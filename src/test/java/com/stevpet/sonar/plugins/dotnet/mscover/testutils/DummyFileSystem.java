package com.stevpet.sonar.plugins.dotnet.mscover.testutils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.test.TestUtils;

public class DummyFileSystem implements ProjectFileSystem {

    private File baseDir =TestUtils.getResource("/") ;
    
    public ProjectFileSystem addSourceDir(File arg0) {
        return null;
    }

    public ProjectFileSystem addTestDir(File arg0) {
        return null;
    }

    public File getBasedir() {
        return baseDir;

    }

    public void  setBasedir(File dir) {
        baseDir=dir;
    }
    public File getBuildDir() {
        return null;
    }

    public File getBuildOutputDir() {
        return null;
    }

    public File getFileFromBuildDirectory(String arg0) {
        return null;
    }

    public List<File> getJavaSourceFiles() {
        return null;
    }

    public File getReportOutputDir() {
        return null;
    }

    public File getSonarWorkingDirectory() {
        return null;
    }

    public Charset getSourceCharset() {
        return Charset.availableCharsets().get("UTF-8");
    }

    public List<File> getSourceDirs() {
        String root = "TfsBlame/tfsblame/" ;
        List<File> list = new ArrayList<File>();
        String[] dirs = {"tfsblame","UnitTests"};
        for(String dir : dirs) {
            list.add(TestUtils.getResource(root + dir ));
        }
        return list;
       
    }

    public List<File> getSourceFiles(Language... arg0) {
        return null;
    }

    public List<File> getTestDirs() {
        return null;
    }

    public List<File> getTestFiles(Language... arg0) {
        return null;
    }

    public boolean hasJavaSourceFiles() {
        return false;
    }

    public boolean hasTestFiles(Language arg0) {
        return false;
    }

    public List<InputFile> mainFiles(String... arg0) {
        return null;
    }

    public File resolvePath(String arg0) {
        return null;
    }

    public List<InputFile> testFiles(String... arg0) {

        return null;
    }

    public Resource<?> toResource(File arg0) {

        return null;
    }

    public File writeToWorkingDirectory(String arg0, String arg1)
            throws IOException {
        return null;
    }
      
  }