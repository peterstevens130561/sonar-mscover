/*
 * Sonar .NET Plugin :: Gallio
 * Copyright (C) 2010 Jose Chillan, Alexandre Victoor and SonarSource
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.in.SMInputCursor;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.resources.Project;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.VSTestCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestLinesToCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestSourceFileNamesToCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.IntegrationTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;


public class MSCoverResultParserTest {


  @Test 
  public void ParseWithCoverageParser_ForValidFile_CheckRegistry() throws XMLStreamException {
        //Arrange
        File file = getResource("mscoverage.xml");
        CoverageParserSubject parser = new CoverageParserSubject();

        TestCoverageRegistry coverageRegistry = new TestCoverageRegistry() ;
        VsTestLinesToCoverageObserver observer = new VsTestLinesToCoverageObserver();
        observer.setRegistry(coverageRegistry);
        
        //Act
        
        parser.registerObserver(observer);
        parser.parseFile(file);
        //Assert
        Assert.assertEquals(31,coverageRegistry.getVisitedLines());
        
  }  
  
  @Test 
  public void ParseWithCoverageParser_ForValidFile_CheckFilesRegistry() throws XMLStreamException {
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
  public void Parse_ForValidSmallFileWithRealRegistrySave_ShouldHaveRightDetailsAndSummary() throws XMLStreamException, IOException {
      // given a proper coverage file
        //Arrange
        File file = getResource("mscoverage.xml");
        //File file = new File("C:/Development/Jewel.Release.Oahu/specflow.xml");
        // verify that it is compatible
        Project project = new Project("tfsblame","","tfsBlame");

        File projectDir= getResource("TfsBlame/tfsblame");
        
        FileSystemMock fileSystemMock = new FileSystemMock();
        fileSystemMock.givenWorkDir(new File(projectDir,".sonar"));
        fileSystemMock.givenBaseDir(projectDir);
        
        //Act
        VSTestCoverageSaver coverageHelper = VSTestCoverageSaver.create(fileSystemMock.getMock());
        
        LineMeasureSaver lineSaver = mock(IntegrationTestLineSaver.class);
        coverageHelper.setLineSaver(lineSaver);
        List<String> modules = new ArrayList<String>();

        coverageHelper.analyse(project, file.getCanonicalPath(),modules);
        //Assert ?
        verify(lineSaver,times(8)).saveMeasures(any(FileLineCoverage.class), any(File.class));
  }

  
  @Test 
  public void Parse_ForValidSmallFileWithRealRegistrySave_OnlySpecifiedAssembly() throws XMLStreamException, IOException {
      // given a proper coverage file
        //Arrange
        File file = getResource("mscoverage.xml");
        //File file = new File("C:/Development/Jewel.Release.Oahu/specflow.xml");
        // verify that it is compatible
        Project project = new Project("tfsblame","","tfsBlame");

        File projectDir= getResource("TfsBlame/tfsblame");
        
        FileSystemMock fileSystemMock = new FileSystemMock();
        fileSystemMock.givenWorkDir(new File(projectDir,".sonar"));
        fileSystemMock.givenBaseDir(projectDir);
        
        //Act
        VSTestCoverageSaver coverageHelper = VSTestCoverageSaver.create(fileSystemMock.getMock());
        
        LineMeasureSaver lineSaver = mock(IntegrationTestLineSaver.class);
        coverageHelper.setLineSaver(lineSaver);
        List<String> modules = new ArrayList<String>();
        modules.add("rabarber.exe");
        coverageHelper.analyse(project, file.getCanonicalPath(),modules);
        //Assert ?
        verify(lineSaver,times(8)).saveMeasures(any(FileLineCoverage.class), any(File.class));
  }

private File getResource(String resourcePath) {
    File resourceFile= TestUtils.getResource(resourcePath);
    Assert.assertTrue("File " + resourcePath + " does not exist",resourceFile.exists());
    return resourceFile;
}
 

public void Line(SMInputCursor linesCursor) {   
}

public void SourceFileNames(SMInputCursor childCursor) {
 
}

}

