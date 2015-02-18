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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import java.io.File;
import java.util.Collection;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.TestCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SolutionLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.DefaultSolutionLineCoverage;
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

        SolutionLineCoverage coverageRegistry = new DefaultSolutionLineCoverage("c:\\Users\\stevpet\\Documents\\GitHub\\tfsblame") ;
        VsTestSourceFileNamesToCoverageObserver observer = new VsTestSourceFileNamesToCoverageObserver();
        observer.setRegistry(coverageRegistry);

        //Act
        parser.registerObserver(observer);
        parser.parseFile(file);
        //Assert
        Assert.assertEquals(8,coverageRegistry.getFileCount());

        Collection<FileLineCoverage> fileCoverageCollection=coverageRegistry.getFileCoverages();
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
        for (FileLineCoverage fileCoverage : fileCoverageCollection) {
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
