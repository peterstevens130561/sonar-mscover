/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.test.MutableTestCase;
import org.sonar.api.test.MutableTestPlan;
import org.sonar.api.test.TestCase.Status;
import org.sonar.core.test.DefaultTestPlan;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;

public class VsTestResultsSaverSociableTest {

    private VsTestTestResultsSaver saver ;
    @Mock private PathResolver pathResolver;
    @Mock private FileSystem fileSystem;
    @Mock private ResourcePerspectives perspectives;
    @Mock private MutableTestPlan testPlan;
    @Mock private SensorContext sensorContext;
    private ProjectUnitTestResults results;
    private org.sonar.api.resources.File sonarFile;
    @Mock private DefaultResourceResolver resourceResolver;
    @Mock private MutableTestCase testCase; 
    
    
    @Before
    public void before() {
        testPlan = new DefaultTestPlan();

        org.mockito.MockitoAnnotations.initMocks(this);
        when(perspectives.as(eq(MutableTestPlan.class), any( org.sonar.api.resources.File.class))).thenReturn(testPlan);
        saver = new VsTestTestResultsSaver(resourceResolver, perspectives);
        when(testCase.setType(any())).thenReturn(testCase);
        when(testCase.setMessage(any())).thenReturn(testCase);
        when(testCase.setStackTrace(any())).thenReturn(testCase);  
        when(testCase.setDurationInMs(anyLong())).thenReturn(testCase);   
        when(testCase.setStatus(any())).thenReturn(testCase);   
    }
    
    @Test
    public void fileNotKnown() {
        when(perspectives.as(eq(MutableTestPlan.class), any( org.sonar.api.resources.File.class))).thenReturn(null);
        setupForStatus("Failed");
        verify(perspectives,times(1)).as(eq(MutableTestPlan.class), eq(sonarFile));
        verify(testPlan,times(0)).addTestCase("first");
        
    }
    
    @Test
    public void integrationTestFile() {
        results= new ProjectUnitTestResults();

        sonarFile = new org.sonar.api.resources.File("sources","first.feature.cs");
        File first = new File("first");
        when(testPlan.addTestCase("first")).thenReturn(testCase);
        ClassUnitTestResult result = results.addFile(first);
        when(resourceResolver.getFile(first)).thenReturn(sonarFile);
        UnitTestMethodResult unitTest = new UnitTestMethodResult();
        unitTest.setClassName("class").setMessage("message").setTestName("first").setTimeMicros(25000).setOutcome("Passed").setStackTrace("stacktrace");
        result.add(unitTest);
        saver.save(sensorContext, results);


        verify(perspectives,times(1)).as(eq(MutableTestPlan.class), eq(sonarFile));
        verify(testPlan,times(1)).addTestCase("first");
        verify(testCase,times(1)).setMessage("message");
        verify(testCase,times(1)).setStatus(Status.OK);
        verify(testCase,times(1)).setDurationInMs(25l);
        verify(testCase,times(1)).setType("INTEGRATION");  
        verify(testCase,times(1)).setStackTrace("stacktrace");      
    }
    @Test
    public void oneFile() {
        results= new ProjectUnitTestResults();
        createFirstFile();
        File first = new File("first");
        when(testPlan.addTestCase("first")).thenReturn(testCase);
        ClassUnitTestResult result = results.addFile(first);
        when(resourceResolver.getFile(first)).thenReturn(sonarFile);
        UnitTestMethodResult unitTest = new UnitTestMethodResult();
        unitTest.setClassName("class").setMessage("message").setTestName("first").setTimeMicros(25000).setOutcome("Passed").setStackTrace("stacktrace");
        result.add(unitTest);
        saver.save(sensorContext, results);


        verify(perspectives,times(1)).as(eq(MutableTestPlan.class), eq(sonarFile));
        verify(testPlan,times(1)).addTestCase("first");
        verify(testCase,times(1)).setMessage("message");
        verify(testCase,times(1)).setStatus(Status.OK);
        verify(testCase,times(1)).setDurationInMs(25l);
        verify(testCase,times(1)).setType("UNIT");        
        verify(testCase,times(1)).setStackTrace("stacktrace");      
    }
    
    @Test
    public void failed() {
        setupForStatus("Failed");
        verify(testCase,times(1)).setStatus(Status.FAILURE);
    }
    
    @Test
    public void ignored() {
        setupForStatus("NotExecuted");
        verify(testCase,times(1)).setStatus(Status.SKIPPED);
    }
    
    private void setupForStatus(String outcome) {
        results= new ProjectUnitTestResults();
        createFirstFile();
        File first = new File("first");
        when(testPlan.addTestCase("first")).thenReturn(testCase);
        ClassUnitTestResult result = results.addFile(first);
        when(resourceResolver.getFile(first)).thenReturn(sonarFile);
        UnitTestMethodResult unitTest = new UnitTestMethodResult();
        unitTest.setClassName("class").setMessage("message").setTestName("first").setTimeMicros(25000).setOutcome(outcome).setStackTrace("stacktrace");
        result.add(unitTest);
        saver.save(sensorContext, results);
    }
    @Test
    public void twoFiles() {
        results= new ProjectUnitTestResults();
        createFirstFile();
        File first = new File("first");
        when(testPlan.addTestCase("first")).thenReturn(testCase);
        ClassUnitTestResult result = results.addFile(first);
        when(resourceResolver.getFile(first)).thenReturn(sonarFile);
        UnitTestMethodResult unitTest = new UnitTestMethodResult();
        unitTest.setClassName("class").setMessage("message").setTestName("first").setTimeMicros(25000).setOutcome("Passed").setStackTrace("stacktrace");
        result.add(unitTest);
        unitTest = new UnitTestMethodResult();
        when(testPlan.addTestCase("second")).thenReturn(testCase);
        unitTest.setClassName("class").setMessage("message2").setTestName("second").setTimeMicros(5000).setOutcome("Failed").setStackTrace("stacktrace2");
        result.add(unitTest);
        saver.save(sensorContext, results);


        verify(perspectives,times(1)).as(eq(MutableTestPlan.class), eq(sonarFile));
        verify(testPlan,times(1)).addTestCase("first");
        verify(testCase,times(1)).setMessage("message");
        verify(testCase,times(1)).setStatus(Status.OK);
        verify(testCase,times(1)).setDurationInMs(25l);

        verify(testCase,times(1)).setStackTrace("stacktrace");
        
        verify(perspectives,times(1)).as(eq(MutableTestPlan.class), eq(sonarFile));
        verify(testPlan,times(1)).addTestCase("second");
        verify(testCase,times(1)).setMessage("message2");
        verify(testCase,times(1)).setStatus(Status.OK);
        verify(testCase,times(1)).setDurationInMs(5l);
        verify(testCase,times(1)).setStackTrace("stacktrace2");  
        
        verify(testCase,times(2)).setType("UNIT"); 
    }

    private void createFirstFile() {
        sonarFile = new org.sonar.api.resources.File("sources","first");
    }
   
}
