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
package com.stevpet.sonar.plugins.dotnet.mscover;

import org.junit.Assert;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.AntPatternResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;


public class FileFilterTest {
    @Test
    public void NothingSet_ShouldMatch() {
        //Arrange
        ResourceFilter fileFilter = new AntPatternResourceFilter();
        //Act
        boolean isPassed=fileFilter.isPassed("UnitTests1/aap.cpp");
        //Assert
        Assert.assertTrue(isPassed);        
    }
    @Test
    public void UnitTests_FileInDirectory_ShouldMatch() {
        //Arrange
        ResourceFilter fileFilter = new AntPatternResourceFilter();
        fileFilter.setExclusions("*Tests*/**");
        //Act
        boolean isPassed=fileFilter.isPassed("UnitTests1/aap.cpp");
        //Assert
        Assert.assertFalse(isPassed);
    }
    
    @Test
    public void UnitTestsDirSpecified_FileInDirectory_ShouldNotPass() {
        //Arrange
        ResourceFilter fileFilter = new AntPatternResourceFilter();
        fileFilter.setExclusions("*Tests*/*");
        //Act
        boolean isPassed=fileFilter.isPassed("UnitTests1/aap.cpp");
        //Assert
        Assert.assertFalse(isPassed);
    }
    
    @Test
    public void DirectoryMatch_FileInDirectory_ShouldMatch() {
        //Arrange
        ResourceFilter fileFilter = new AntPatternResourceFilter();
        fileFilter.setExclusions("directory/**");
        //Act
        boolean isPassed=fileFilter.isPassed("directory/aap.cpp");
        //Assert
        Assert.assertFalse(isPassed);
    }
    
    @Test
    public void DirectoryMatch_FileNotInDirectory_ShouldNotMatch() {
        //Arrange
        ResourceFilter fileFilter = new AntPatternResourceFilter();
        fileFilter.setExclusions("directory/");
        //Act
        boolean isPassed=fileFilter.isPassed("aap.cpp");
        //Assert
        Assert.assertTrue(isPassed);
        
    }
    
    @Test
    public void FileMatch_FileNotSame_ShouldNotMatch() {
        //Arrange
        ResourceFilter fileFilter = new AntPatternResourceFilter();
        fileFilter.setExclusions("aap.cpp");
        //Act
        boolean isPassed=fileFilter.isPassed("john.cpp");
        //Assert
        Assert.assertTrue(isPassed);
    }
    
    @Test
    public void FileMatch_FileSame_ShouldMatch() {
        //Arrange
        ResourceFilter fileFilter = new AntPatternResourceFilter();
        fileFilter.setExclusions("aap.cpp");
        //Act
        boolean isPassed=fileFilter.isPassed("aap.cpp");
        //Assert
        Assert.assertFalse(isPassed);
    }
    
    @Test
    public void FileMatchMultiple_FileSame_ShouldMatch() {
        //Arrange
        ResourceFilter fileFilter = new AntPatternResourceFilter();
        fileFilter.setExclusions("export/**,*UnitTest/**");
        //Act
        boolean isPassed=fileFilter.isPassed("export/include/aap.h");
        //Assert
        Assert.assertFalse(isPassed);
    }
    
    @Test
    public void FileMatchMultiple_FileNotSame_ShouldMatch() {
        //Arrange
        ResourceFilter fileFilter = new AntPatternResourceFilter();
        fileFilter.setExclusions("export/**,*UnitTest/**");
        //Act
        boolean isPassed=fileFilter.isPassed("export2/include/aap.h");
        //Assert
        Assert.assertTrue(isPassed);
    }
}
