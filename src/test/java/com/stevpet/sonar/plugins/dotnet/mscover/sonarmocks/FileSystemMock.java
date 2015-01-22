package com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks;

import java.io.File;
import java.nio.charset.Charset;
import java.util.SortedSet;
import java.util.TreeSet;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.test.TestUtils;

import static org.mockito.Mockito.when;
import static org.junit.Assert.fail;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class FileSystemMock extends GenericClassMock<FileSystem> {
    public FileSystemMock() {
        super(FileSystem.class);
        //when(instance.languages()).thenReturn(new TreeSet<String>());
    }
    
    public FileSystemMock givenWorkDir(String testResourcePath) {
        File path=TestUtils.getResource(testResourcePath);
        if(!path.exists()) {
            fail(path + " should exist");
        }
        when(instance.workDir()).thenReturn(path);
        return this;
    }

    public void givenWorkDir(File file) {
        when(instance.workDir()).thenReturn(file);    
    }

    public void givenBaseDir(File projectDir) {
        when(instance.baseDir()).thenReturn(projectDir);
    }

    public void givenEncoding(Charset charSet) {
        when(instance.encoding()).thenReturn(charSet);
    }

    public void givenDefaultEncoding() {
        Charset charset = Charset.forName("UTF-8");
        givenEncoding(charset);
        
    }

    public void givenLanguage(String language) {
        SortedSet<String> languageSet = new TreeSet<String>();
        languageSet.add(language);
        when(instance.languages()).thenReturn(languageSet);
    }

}
