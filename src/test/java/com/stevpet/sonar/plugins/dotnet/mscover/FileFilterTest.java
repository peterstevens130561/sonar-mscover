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
