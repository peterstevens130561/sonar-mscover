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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.in.SMInputCursor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import org.sonar.test.TestUtils;

import com.google.common.collect.Lists;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.IntegrationTestBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.VSTestCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestLinesToCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers.VsTestSourceFileNamesToCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.IntegrationTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;

@RunWith(PowerMockRunner.class)
@PrepareForTest({File.class})
public class MSCoverResultParserTest {


  private SensorContext context;
  private Project project;
  private VisualStudioSolution solution;
  private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
  private MsCoverProperties propertiesHelper;
  private Settings settings ;
  

  @Before
  public void setUp() {
    context = mock(SensorContext.class);
    when(context.isIndexed(any(Resource.class), eq(false))).thenReturn(true);
    ProjectFileSystem fileSystem = mock(ProjectFileSystem.class);
    when(fileSystem.getSourceDirs()).thenReturn(Lists.newArrayList(new File("C:\\Work\\CodeQuality\\Temp\\Example")));
    project = mock(Project.class);
    when(project.getFileSystem()).thenReturn(fileSystem);
    when(project.getName()).thenReturn("Example.CoreX"); // we check that assembly/project names are not taken in account
                                                         // (SONARPLUGINS-1517)

    VisualStudioProject vsProject = mock(VisualStudioProject.class);
    when(vsProject.getName()).thenReturn("Example.CoreX");

    solution = mock(VisualStudioSolution.class);
    when(solution.getProject(any(File.class))).thenReturn(vsProject);
    when(solution.getProjectFromSonarProject(eq(project))).thenReturn(vsProject);

    microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
    when(microsoftWindowsEnvironment.getCurrentSolution()).thenReturn(solution);

    
    settings = mock(Settings.class);
    propertiesHelper = PropertiesHelper.create(settings);
    
  }


  
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
        // verify that it is compatible
        Project project = new Project("tfsblame","","tfsBlame");
        ProjectFileSystem fs = mock(ProjectFileSystem.class);
        project.setFileSystem(fs);

        SensorContext sensorContext = mock(SensorContext.class);
        when(sensorContext.isIndexed((Resource<?>) any(), eq(false))).thenReturn(true);
        
        File projectDir= getResource("TfsBlame/tfsblame/tfsblame");
        when(fs.getBasedir()).thenReturn(projectDir);
        when(fs.getSourceCharset()).thenReturn(Charset.forName("UTF-8"));
        
        MeasureSaver measureSaver = mock(SonarMeasureSaver.class);
        //Act
        VSTestCoverageSaver coverageHelper = VSTestCoverageSaver.create(propertiesHelper, microsoftWindowsEnvironment);
        
        LineMeasureSaver lineSaver = mock(IntegrationTestLineSaver.class);
        coverageHelper.setLineSaver(lineSaver);
        BlockMeasureSaver blockMeasureSaver = IntegrationTestBlockSaver.create(measureSaver);
        BlockSaver blockSaver = new BaseBlockSaver(blockMeasureSaver) ;
        coverageHelper.setBlockSaver(blockSaver);
        
        coverageHelper.analyse(project, file.getCanonicalPath());
        //Assert ?
        verify(lineSaver,times(8)).saveMeasures(any(FileCoverage.class), any(File.class));
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

