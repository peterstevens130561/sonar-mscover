package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import java.io.File;
import java.util.Collection;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.TestCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;

public class CoverageSourceFileNamesToSourceFileNamesObserverTest {

    @Test 
    public void ParseFile_ExpectFileCount() throws XMLStreamException {
        //Arrange
        File file = getResource("mscoverage.xml");
        CoverageParserSubject parser = new CoverageParserSubject();

        TestCoverageRegistry coverageRegistry = new TestCoverageRegistry() ;
        VsTestSourceFileNamesToCoverageObserver observer = new VsTestSourceFileNamesToCoverageObserver();
        observer.setRegistry(coverageRegistry);
        
        //Act
        
        parser.registerObserver(observer);
        parser.parseFile(file);
        //Assert
        Assert.assertEquals(8,coverageRegistry.getVisitedFiles());   
  }
    
    
    @Test 
    public void ParseFile_CheckFileNames() throws XMLStreamException {
        //Arrange
        File file = getResource("mscoverage.xml");
        CoverageParserSubject parser = new CoverageParserSubject();

        CoverageRegistry coverageRegistry = new FileCoverageRegistry("c:\\Users\\stevpet\\Documents\\GitHub\\tfsblame") ;
        VsTestSourceFileNamesToCoverageObserver observer = new VsTestSourceFileNamesToCoverageObserver();
        observer.setRegistry(coverageRegistry);

        //Act
        parser.registerObserver(observer);
        parser.parseFile(file);
        //Assert
        Assert.assertEquals(8,coverageRegistry.getFileCount());

        Collection<FileCoverage> fileCoverageCollection=coverageRegistry.getFileCoverages();
        int collectionIndex=0;
        String baseDir="C:\\Users\\stevpet\\Documents\\GitHub\\tfsblame\\";
        String tfsBlameDir=baseDir + "tfsblame\\";
        String[] fileNames = {tfsBlameDir+ "Commits.cs",
                tfsBlameDir+ "Commit.cs",
                tfsBlameDir+ "Blame.cs",
                tfsBlameDir+ "Program.cs",
                tfsBlameDir+ "TfsBlameException.cs",
                tfsBlameDir+ "ServerUriFinder.cs",
                tfsBlameDir+ "Properties\\Settings.Designer.cs",
                tfsBlameDir +"ArgumentParser.cs"};
        for (FileCoverage fileCoverage : fileCoverageCollection) {
            File coverageFile = fileCoverage.getFile();
            Assert.assertNotNull(coverageFile);
            Assert.assertEquals(fileNames[collectionIndex].toLowerCase(), coverageFile.getAbsolutePath().toLowerCase());
            collectionIndex++;
        }
  }
    
    private File getResource(String resourcePath) {
        File resourceFile= TestUtils.getResource(resourcePath);
        Assert.assertTrue("File " + resourcePath + " does not exist",resourceFile.exists());
        return resourceFile;
    }
}
