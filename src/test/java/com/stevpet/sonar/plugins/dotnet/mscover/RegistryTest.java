package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.junit.Assert;
import org.junit.Test;






import com.stevpet.sonar.plugins.dotnet.mscover.listener.CoverageParserListener;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.Parser;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileCoverageRegistry;

public class RegistryTest {

    
    @Test 
    public void Parse_MSCoverage_Commits_CheckNoCoverage() throws XMLStreamException {
          //Arrange
        TestCoverageRegistry coverageRegistry = arrange("mscoverage.xml");
          
        //Act
          FileCoverage fileCoverage = coverageRegistry.getFileCoverageByName("Commits.cs");
          
          //Assert
          Assert.assertEquals(0,fileCoverage.getCoveredLines());
          Assert.assertEquals(24,fileCoverage.getUncoveredLines());
          Assert.assertEquals(24,fileCoverage.getCountLines());        
    }

    
    @Test 
    public void Parse_File2_NotCovered_CheckRegistry() throws XMLStreamException {
          //Arrange
        TestCoverageRegistry coverageRegistry = arrange("coverage.xml");
          
        //Act
          FileCoverage fileCoverage = coverageRegistry.getFileCoverageByName("Program.cs");
          
          //Assert
          Assert.assertEquals(0,fileCoverage.getCoveredLines());
          Assert.assertEquals(30,fileCoverage.getUncoveredLines());
          Assert.assertEquals(30,fileCoverage.getCountLines());        
    }
    
    @Test 
    public void Parse_ServerUriFinder_CheckCoverage() throws XMLStreamException {
          //Arrange
        TestCoverageRegistry coverageRegistry = arrange("coverage.xml");
          
        //Act
          FileCoverage fileCoverage = coverageRegistry.getFileCoverageByName("ServerUriFinder.cs");
          
          //Assert
          Assert.assertEquals(6,fileCoverage.getCoveredLines());
          Assert.assertEquals(3,fileCoverage.getUncoveredLines());
          Assert.assertEquals(9,fileCoverage.getCountLines());        
    }

    private TestCoverageRegistry arrange(String coverageName) throws XMLStreamException {
        File file = Helper.getResource(coverageName);
        Parser parser = new CoverageParser();
        SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
        SMHierarchicCursor rootCursor = inf.rootElementCursor(file);
        SMInputCursor root = rootCursor.advance();
        
        CoverageParserListener parserListener = new CoverageParserListener();
        TestCoverageRegistry coverageRegistry = new TestCoverageRegistry() ;
        parserListener.setRegistry(coverageRegistry);
        
        //Act
        parser.setListener(parserListener);
        parser.parse(root);
        return coverageRegistry;
    }


    
    private class TestCoverageRegistry extends FileCoverageRegistry {

        public TestCoverageRegistry() {
            super("c:\\Users\\stevpet\\Documents\\GitHub\\tfsblame\\tfsblame");
            // TODO Auto-generated constructor stub
        }
             
        public FileCoverage getFileCoverageByName(String name) {
            for(FileCoverage coverage : getRegistry().values()) {
                File file = coverage.getFile();
                if(file!=null && file.getName().endsWith(name)) {
                    return coverage ;
                }
            }
            return null;
        }
        
    }
}
