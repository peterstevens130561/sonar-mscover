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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestSourceFileNamesToSourceFileNamesObserver;

public class SourceFileNamesObserverTest {
    
    private static final String SOURCE_FILE_NAMES = "SourceFileNames/";
    private static final String SOURCE_FILE_NAME = "SourceFileName" ;
    private static final String SOURCE_FILE_ID = "SourceFileID";
    private static final String SOURCE_FILE_NAME_PATH = SOURCE_FILE_NAMES + SOURCE_FILE_NAME;
    private static final String SOURCE_FILE_ID_PATH = SOURCE_FILE_NAMES + SOURCE_FILE_ID;
    VsTestSourceFileNamesToSourceFileNamesObserver observer = new VsTestSourceFileNamesToSourceFileNamesObserver();
    @Test
    public void CompleteSourceFileID_ShouldGetSame() {
        String path = SOURCE_FILE_ID_PATH;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test
    public void CompleteSourceFileName_ShouldGetSame() {
        String path = SOURCE_FILE_NAME_PATH;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test
    public void OtherField_ShouldNotMatch() {
        String path = "SourceFileName";
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertFalse(isMatch);
    }
    
    @Test
    public void TwoFileNames_ShouldBeTwoInRegistry() {
        SourceFileNameTable registry = new SourceFileNameTable();
        observer.setRegistry(registry) ;
        observer.observeElement(SOURCE_FILE_ID, "1");
        observer.observeElement(SOURCE_FILE_NAME, "a/b/c");
        observer.observeElement(SOURCE_FILE_ID, "2");
        observer.observeElement(SOURCE_FILE_NAME, "a/b/d");
        Assert.assertEquals(2,registry.size());
    }
    
    @Test
    public void ParseFileWithObserver() throws XMLStreamException {
        //Arrange
        XmlParserSubject parser = new CoverageParserSubject();
        SourceFileNameTable registry = new SourceFileNameTable();
        observer.setRegistry(registry);
        parser.registerObserver(observer);
        
        File file=TestUtils.getResource("mscoverage.xml");
        //Act
        parser.parseFile(file);
        //Assert
        Assert.assertEquals(8,registry.size());
    }
    
}
