package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.BlockModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileBlocks;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.MethodBlocksObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;

public class MethodBlocksObserverTest {
    //Arrange
    MethodBlocksObserver observer = new MethodBlocksObserver();
    @Test 
    public void MatchBlocksCovered_ShouldMatch() {
        String path = "Module/NamespaceTable/Class/Method/BlocksCovered";
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchBlocksNotCovered_ShouldMatch() {
        String path = "Module/NamespaceTable/Class/Method/BlocksNotCovered";
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchsourceFileId_ShouldMatch() {
        String path = "Module/NamespaceTable/Class/Method/Lines/SourceFileID";
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchsourceLnStart_ShouldMatch() {
        String path = "Module/NamespaceTable/Class/Method/Lines/LnStart";
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchsourceLnEnd_ShouldNotMatch() {
        String path = "Module/NamespaceTable/Class/Method/Lines/LnEnd";
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertFalse(isMatch);
    }
    @Test 
    public void MatchLines_ShouldNotMatch() {
        String path = "Module/NamespaceTable/Class/Method/Lines";
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertFalse(isMatch);
    }
    
    @Test 
    public void MatchSomeStuff_ShouldNotMatch() {
        String path = "Module/NamespaceTable/Class/Method/";
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertFalse(isMatch);
    }
    
    @Test
    public void SimpleSequence_ShouldSave() {
        //Arrange
        FileBlocksRegistry registry = new FileBlocksRegistry();
        observer.setRegistry(registry);
        //Act
        observer.observeElement("BlocksCovered", "10");
        observer.observeElement("BlocksNotCovered","5");
        observer.observeElement("LnStart","20");
        observer.observeElement("SourceFileID","25");
        observer.observeElement("LnStart","21");
        observer.observeElement("SourceFileID", "25");
        //Assert
        FileBlocks fileBlocks=registry.get("25");
        Assert.assertEquals(1,fileBlocks.getBlocks().size());
        BlockModel block=fileBlocks.getBlocks().get(0);
        Assert.assertEquals(10, block.getCovered());
        Assert.assertEquals(5, block.getNotCovered());
        Assert.assertEquals(20,block.getLine());
    }
    
    @Test
    public void ParseFileWithObserver() throws XMLStreamException {
        //Arrange
        ParserSubject parser = new CoverageParserSubject();
        FileBlocksRegistry registry = new FileBlocksRegistry();
        observer.setRegistry(registry);
        parser.registerObserver(observer);
        
        File file=TestUtils.getResource("mscoverage.xml");
        //Act
        parser.parseFile(file);
        //Assert
        Assert.assertEquals(8,registry.values().size());
    }
}
