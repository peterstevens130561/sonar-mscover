/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks;

import java.io.File;
import java.nio.charset.Charset;
import java.util.SortedSet;
import java.util.TreeSet;

import org.sonar.api.batch.fs.FileSystem;
import static org.mockito.Mockito.when;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class FileSystemMock extends GenericClassMock<FileSystem> {
    public FileSystemMock() {
        super(FileSystem.class);
        //when(instance.languages()).thenReturn(new TreeSet<String>());
    }
    
    public FileSystemMock givenWorkDir(String testResourcePath) {
        File path=new File(testResourcePath);
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
