package com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks;

import java.io.File;

import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.test.TestUtils;
import static org.mockito.Mockito.when;
import static org.junit.Assert.fail;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class ModuleFileSystemMock extends GenericClassMock<ModuleFileSystem> {
    public ModuleFileSystemMock() {
        super(ModuleFileSystem.class);
    }
    
    public ModuleFileSystemMock givenWorkingDir(String testResourcePath) {
        File path=TestUtils.getResource(testResourcePath);
        if(!path.exists()) {
            fail(path + " should exist");
        }
        when(instance.workingDir()).thenReturn(path);
        return this;
    }
    
}
