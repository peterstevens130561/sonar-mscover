package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverProgrammerException;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.exceptions.MsCoverInvalidSonarWorkingDir;

public class TestResultsCleaner {
    
    private FileSystem fileSystem;
    
    public TestResultsCleaner(FileSystem fileSystem) {
        this.fileSystem=fileSystem;
    }
    
    public void execute() {
        File sonarDir = fileSystem.workDir();
        if (sonarDir == null) {
            throw new MsCoverProgrammerException("sonarPath not set");
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
