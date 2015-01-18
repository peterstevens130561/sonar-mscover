package com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks;

import java.io.File;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.test.TestUtils;

import static org.mockito.Mockito.when;
import static org.junit.Assert.fail;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class FileSystemMock extends GenericClassMock<FileSystem> {
    public FileSystemMock() {
        super(FileSystem.class);
    }
    
    public FileSystemMock givenWorkDir(String testResourcePath) {
        File path=TestUtils.getResource(testResourcePath);
        if(!path.exists()) {
            fail(path + " should exist");
        }
        when(instance.workDir()).thenReturn(path);
        return this;
    }
    
}
