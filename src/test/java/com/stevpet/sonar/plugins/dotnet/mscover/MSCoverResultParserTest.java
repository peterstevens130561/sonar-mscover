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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
//import com.google.inject.internal.util.Lists;

import com.google.common.collect.Lists;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.IntegrationTestBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.listener.CoverageParserListener;
import com.stevpet.sonar.plugins.dotnet.mscover.listener.ParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.Parser;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.SingleListenerParser;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.IntegrationTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.sonar.test.TestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({File.class})
public class MSCoverResultParserTest {


  private SensorContext context;
  private Project project;
  private VisualStudioSolution solution;
  private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
  private PropertiesHelper propertiesHelper;
  private Settings settings ;
  
  private TimeMachine timeMachine ;

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
    propertiesHelper = new PropertiesHelper(settings);
    
    timeMachine = mock(TimeMachine.class);
  }

  @Test 
  public void IsCompatible_ForValidFile_True() throws XMLStreamException {
	  // given a proper coverage file
	    File file = getResource("mscoverage.xml");
	    Parser parser = new SingleListenerParser();
	    SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
	    SMHierarchicCursor rootCursor = inf.rootElementCursor(file);
	    SMInputCursor root = rootCursor.advance();
	    // verify that it is compatible
	    boolean isCompatible=parser.isCompatible(root);
	    Assert.assertTrue(isCompatible);
  }
  
  @Test 
  public void Parse_ForValidFile_CheckFiles() throws XMLStreamException {
	    //Arrange
	    File file = getResource("mscoverage.xml");
	    Parser parser = new SingleListenerParser();
	    SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
	    SMHierarchicCursor rootCursor = inf.rootElementCursor(file);
	    SMInputCursor root = rootCursor.advance();	    
	    TestCoverageParserListener parserListener = new TestCoverageParserListener();
	    parser.setListener(parserListener);
	    //Act
	    parser.parse(root);
	    //Assert
        Assert.assertEquals(8, parserListener.getVisitedFiles());
	    Assert.assertEquals(334,parserListener.getVisitedLines());
  }
  
  @Test 
  public void Parse_ForValidFile_CheckRegistry() throws XMLStreamException {
        //Arrange
        File file = getResource("mscoverage.xml");
        Parser parser = new SingleListenerParser();
        SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
        SMHierarchicCursor rootCursor = inf.rootElementCursor(file);
        SMInputCursor root = rootCursor.advance();
        
        CoverageParserListener parserListener = new CoverageParserListener();
        TestCoverageRegistry coverageRegistry = new TestCoverageRegistry() ;
        parserListener.setRegistry(coverageRegistry);
        
        //Act
        parser.setListener(parserListener);
        parser.parse(root);
        //Assert
        Assert.assertEquals(8, coverageRegistry.getVisitedFiles());
        Assert.assertEquals(31,coverageRegistry.getVisitedLines());
        
  }
  
  
  @Test 
  public void Parse_ForValidFileWithRealRegistry_ShouldHaveCoveredItems() throws XMLStreamException {
          //Arrange
        File file = getResource("mscoverage.xml");
        //Act
        CoverageRegistry coverageRegistry = parseFile(file);
        //Assert
        Assert.assertEquals(8, coverageRegistry.getFileCount());
        Assert.assertEquals(31, coverageRegistry.getCoveredLineCount());
        Assert.assertEquals(167,coverageRegistry.getLineCount());
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
        when(sensorContext.isIndexed((Resource) any(), eq(false))).thenReturn(true);
        
        File projectDir= getResource("TfsBlame/tfsblame/tfsblame");
        when(fs.getBasedir()).thenReturn(projectDir);
        when(fs.getSourceCharset()).thenReturn(Charset.forName("UTF-8"));
        
        //Act
        CoverageHelper coverageHelper = CoverageHelper.create(propertiesHelper, microsoftWindowsEnvironment);
        
        LineMeasureSaver lineSaver = mock(IntegrationTestLineSaver.class);
        coverageHelper.setLineSaver(lineSaver);
        ResourceMediator resourceMediator = ResourceMediator.create(sensorContext,project);
        BlockMeasureSaver blockMeasureSaver = new IntegrationTestBlockSaver();
        BlockSaver blockSaver = new BaseBlockSaver(sensorContext,project,resourceMediator, blockMeasureSaver) ;
        coverageHelper.setBlockSaver(blockSaver);
        
        coverageHelper.analyse(project, sensorContext, file.getCanonicalPath(),resourceMediator);
        //Assert ?
        verify(lineSaver,times(8)).saveSummaryMeasures(any(SensorContext.class), any(FileCoverage.class), any(Resource.class));
        verify(lineSaver,times(8)).getHitData(any(FileCoverage.class));
  }

  private CoverageRegistry parseFile(File file) throws FactoryConfigurationError,
        XMLStreamException {
    Parser parser = new SingleListenerParser();
    SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
    SMHierarchicCursor rootCursor = inf.rootElementCursor(file);
    SMInputCursor root = rootCursor.advance();
    // verify that it is compatible
    CoverageParserListener parserListener = new CoverageParserListener();
    String projectDir = getResource("TfsBlame/tfsblame/tfsblame").getAbsolutePath();
    CoverageRegistry coverageRegistry = new FileCoverageRegistry(projectDir) ;
    parserListener.setRegistry(coverageRegistry);
    parser.setListener(parserListener);
    parser.parse(root);
    return coverageRegistry;
}
  private class TestCoverageParserListener implements ParserObserver {

    private int visitedLines;
    private int visitedFiles;

    int getVisitedLines() {
        return visitedLines;
    }
    
    int getVisitedFiles() {
        return visitedFiles;
    }
    public void onLine(SMInputCursor cursor) {
        visitedLines++;
    }

    public void onSourceFileNames(SMInputCursor cursor) {
        visitedFiles++;
    }

    public boolean onModuleName(SMInputCursor cursor) {
        return true;
    }
      
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

