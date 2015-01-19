package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLines;
import com.stevpet.sonar.plugins.dotnet.mscover.model.LineModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.LinesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestLinesToLinesObserver;



public class LinesOberverTest {
    private static final String PATTERN_LN_END = "Module/NamespaceTable/Class/Method/Lines/LnEnd";
    private static final String PATTERN_SOURCEFILEID = "Module/NamespaceTable/Class/Method/Lines/SourceFileID";
    private static final String PATTERN_COVERAGE = "Module/NamespaceTable/Class/Method/Lines/Coverage";
    private static final String PATTERN_LNSTART = "Module/NamespaceTable/Class/Method/Lines/LnStart";
    
    private FileLines fileLines;
    VsTestLinesToLinesObserver observer = new VsTestLinesToLinesObserver();
    @Test 
    public void MatchLnStart_ShouldMatch() {
        String path = PATTERN_LNSTART;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchCoverage_ShouldMatch() {
        String path = PATTERN_COVERAGE;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchSourceFileID_ShouldMatch() {
        String path = PATTERN_SOURCEFILEID;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchLnEnd_ShouldNotMatch() {
        String path = PATTERN_LN_END;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertFalse(isMatch);
    }
    
    @Test
    public void ParseFileWithObserver() throws XMLStreamException {
        //Arrange
        XmlParserSubject parser = new CoverageParserSubject();
        LinesRegistry registry = new LinesRegistry();
        observer.setRegistry(registry);
        parser.registerObserver(observer);
        
        File file=TestUtils.getResource("mscoverage.xml");
        //Act
        parser.parseFile(file);
        //Assert
        int files = registry.size();
        Assert.assertEquals(8,files);
        fileLines=registry.get("1");
        Assert.assertEquals(25, fileLines.get().size());
       
        assertLine(0,false,61);
        assertLine(1,false,62);    
        
        fileLines=registry.get("8");
        Assert.assertEquals(26, fileLines.get().size());
        assertLine(0,true,37);
        assertLine(1,true,38);
       
    }
    private void assertLine(int line, boolean isCovered,int lnStart) {
        LineModel lineModel=fileLines.get().get(line);
        Assert.assertEquals(isCovered,lineModel.isCovered());
        Assert.assertEquals(lnStart, lineModel.getLnStart());
    }
}
