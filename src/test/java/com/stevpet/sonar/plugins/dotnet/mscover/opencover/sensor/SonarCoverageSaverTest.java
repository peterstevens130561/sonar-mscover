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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.AnnotatedFieldInjection;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarBranchSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;

public class SonarCoverageSaverTest {
    
    private SonarCoverage sonarCoverageRegistry= new SonarCoverage();
    private MeasureSaver measureSaver = mock(MeasureSaver.class);
    private DefaultPicoContainer picoContainer = new DefaultPicoContainer(new AnnotatedFieldInjection());
    private SonarCoverageSaver coverageSaver;
    @Before
    public void before() {
        picoContainer.addComponent(SonarCoverageSaver.class);
        picoContainer.addComponent(SonarBranchSaver.class);
        picoContainer.addComponent(SonarLineSaver.class);
        picoContainer.addComponent(measureSaver);
        picoContainer.addComponent(sonarCoverageRegistry);
        coverageSaver = picoContainer.getComponent(SonarCoverageSaver.class);
    }
    
    @Test
    public void create_ExpectObject() {
        assertNotNull(coverageSaver);
    }
    
    @Test
    public void simpleSave() {

        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(sonarCoverageRegistry);
        File file = TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);   

        
        //coverageSaver.setCoverageRegistry(sonarCoverageRegistry);
        coverageSaver.save();
        verify(measureSaver,times(93)).saveFileMeasure(any(Measure.class));
        verify(measureSaver,times(62)).setFile(any(File.class)); 
        
        verify(measureSaver,times(0)).saveFileMeasure(eq(CoreMetrics.LINES), anyDouble());
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.LINES_TO_COVER), anyDouble());
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.CONDITIONS_TO_COVER), anyDouble());
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.UNCOVERED_CONDITIONS), anyDouble());
    }
    
    @Test
    public void simpleSaveWithExclusion() {

        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(sonarCoverageRegistry);
        //Given parsed file coverage=report.xml
        File file = TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);    
        //coverageSaver.setCoverageRegistry(sonarCoverageRegistry);
        //Given I exclude file
        List<File> testFiles = new ArrayList<File>();
        testFiles.add(new File("c:/Development/Jewel.Release.Oahu/JewelEarth/Core/ThinClient/WinForms/ViewHost.cs"));
        coverageSaver.setExcludeSourceFiles(testFiles);
        //When I save
        coverageSaver.save();
        //Then
        verify(measureSaver,times(90)).saveFileMeasure(any(Measure.class));
        verify(measureSaver,times(60)).setFile(any(File.class)); 
        
        verify(measureSaver,times(0)).saveFileMeasure(eq(CoreMetrics.LINES), anyDouble());
        verify(measureSaver,times(30)).saveFileMeasure(eq(CoreMetrics.LINES_TO_COVER), anyDouble());
        verify(measureSaver,times(30)).saveFileMeasure(eq(CoreMetrics.CONDITIONS_TO_COVER), anyDouble());
        verify(measureSaver,times(30)).saveFileMeasure(eq(CoreMetrics.UNCOVERED_CONDITIONS), anyDouble());
    }
}
