package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;

public class SourceFileNamesObserverTest {
    
    private static final String SOURCE_FILE_NAMES = "SourceFileNames/";
    private static final String SOURCE_FILE_NAME = "SourceFileName" ;
    private static final String SOURCE_FILE_ID = "SourceFileID";
    private static final String SOURCE_FILE_NAME_PATH = SOURCE_FILE_NAMES + SOURCE_FILE_NAME;
    private static final String SOURCE_FILE_ID_PATH = SOURCE_FILE_NAMES + SOURCE_FILE_ID;
    CoverageSourceFileNamesObserver observer = new CoverageSourceFileNamesObserver();
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
        SourceFileNamesRegistry registry = new SourceFileNamesRegistry();
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
        ParserSubject parser = new CoverageParserSubject();
        SourceFileNamesRegistry registry = new SourceFileNamesRegistry();
        observer.setRegistry(registry);
        parser.registerObserver(observer);
        
        File file=TestUtils.getResource("mscoverage.xml");
        //Act
        parser.parseFile(file);
        //Assert
        Assert.assertEquals(8,registry.size());
    }
    
}
