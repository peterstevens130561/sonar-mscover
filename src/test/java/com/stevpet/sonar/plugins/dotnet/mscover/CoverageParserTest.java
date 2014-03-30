package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import static org.mockito.Mockito.mock;

import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.listener.ParserListener;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.Parser;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.CoverageParser;


public class CoverageParserTest {

    Settings settings ;
    @Before
    public void before() {
        settings = mock(Settings.class);
    }
    
    @Test
    public void ExcludeDefined_LinesNotScanned() throws XMLStreamException {
        File file = Helper.getResource("mscoverage.xml");
        Parser parser = new CoverageParser();
        SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
        SMHierarchicCursor rootCursor = inf.rootElementCursor(file);
        SMInputCursor root = rootCursor.advance();      
        TestCoverageParserListener parserListener = new TestCoverageParserListener();
        parser.setListener(parserListener);
        //Act
        parser.parse(root);
        //Assert
        Assert.assertEquals(8, parserListener.getVisitedFiles());
        Assert.assertEquals(0,parserListener.getVisitedLines());
    }
    
    
    
    private class TestCoverageParserListener implements ParserListener {
        
        
        private int visitedLines;
        private int visitedFiles;

        int getVisitedLines() {
            return visitedLines;
        }
        
        int getVisitedFiles() {
            return visitedFiles;
        }
        public void onLine(SMInputCursor linesCursor) {
            // TODO Auto-generated method stub
            visitedLines++;
        }

        public void onSourceFileNames(SMInputCursor childCursor) {
            // TODO Auto-generated method stub
            visitedFiles++;
        }

        public boolean onModuleName(String moduleName) {
            return(!"tfsblame.exe".equals(moduleName));
        }

          
      }
}
