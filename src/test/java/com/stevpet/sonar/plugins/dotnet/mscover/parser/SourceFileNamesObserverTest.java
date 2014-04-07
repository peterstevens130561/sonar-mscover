package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.io.File;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;

public class SourceFileNamesObserverTest {
    
    private static final String SOURCE_FILE_NAMES = "SourceFileNames/";
    private static final String SOURCE_FILE_NAME = "SourceFileName" ;
    private static final String SOURCE_FILE_ID = "SourceFileID";
    private static final String SOURCE_FILE_NAME_PATH = SOURCE_FILE_NAMES + SOURCE_FILE_NAME;
    private static final String SOURCE_FILE_ID_PATH = SOURCE_FILE_NAMES + SOURCE_FILE_ID;
    SourceFileNamesObserver observer = new SourceFileNamesObserver();
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
        observer.observe(SOURCE_FILE_ID, "1");
        observer.observe(SOURCE_FILE_NAME, "a/b/c");
        observer.observe(SOURCE_FILE_ID, "2");
        observer.observe(SOURCE_FILE_NAME, "a/b/d");
        Assert.assertEquals(2,registry.size());
    }
    
    @Test
    public void ParseFileWithObserver() throws XMLStreamException {
        //Arrange
        ParserSubject parser = new ParserSubject();
        SourceFileNamesRegistry registry = new SourceFileNamesRegistry();
        observer.setRegistry(registry);
        parser.registerObserver(observer);
        
        File file=TestUtils.getResource("mscoverage.xml");
        SMInputCursor cursor = getCursor(file);
        //Act
        parser.parse(cursor);
        //Assert
        Assert.assertEquals(8,registry.size());
    }
    
    private SMInputCursor getCursor(File file)
            throws FactoryConfigurationError, XMLStreamException {
        SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
        SMHierarchicCursor cursor= inf.rootElementCursor(file);
        return cursor.advance();
    }
}
