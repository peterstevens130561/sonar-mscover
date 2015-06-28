package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;


public class ReSharperSensorTest {

    Sensor sensor ;
    @Mock private FileSystem fileSystem;
    @Mock private Settings settings;
    @Mock private InspectCodeResultsParser inspectCodeResultsParser;
    @Mock private InspectCodeIssuesSaver inspectCodeIssuesSaver;
    @Mock private InspectCodeRunner inspectCodeRunner;
    @Mock private Project project;
    @Mock private SensorContext context;
    private SortedSet<String> languages;
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        sensor = new ReSharperSensor(fileSystem, settings, inspectCodeResultsParser, inspectCodeIssuesSaver, inspectCodeRunner);
        languages = new TreeSet<String>();
        when(fileSystem.languages()).thenReturn(languages);
        when(project.isRoot()).thenReturn(true);
    }
    
    @Test
    public void NormalCSharp_Enable_ShouldRun() {
        //Given language is cs
        languages.add("cs");
        //When
        boolean execute=sensor.shouldExecuteOnProject(project);
        //Then
        assertTrue("language contains cs, skip mode not specified, should execute",execute);
        
    }
    
    @Test
    public void JavaScript_Enable_ShouldNotRun() {
        //Given language is javascript
        languages.add("js");
        //When
        boolean execute=sensor.shouldExecuteOnProject(project);
        //Then
        assertFalse("language contains js, skip mode not specified, should not execute",execute);
        
    }
    
    @Test
    public void Cs_Disable_ShouldNotRun() {
        //Given language is cs
        languages.add("cs");
        when(settings.getString(ReSharperConstants.MODE)).thenReturn(ReSharperConstants.MODE_SKIP);
        //When
        boolean execute=sensor.shouldExecuteOnProject(project);
        //Then
        assertFalse("language contains cs, skip mode, should not execute",execute);
        
    }
    
    @Test
    public void Cs_CPP_Enable_ShouldRun() {
        //Given language is cs
        languages.add("cs");
        languages.add("c++");
        when(settings.getString(ReSharperConstants.MODE)).thenReturn("");
        //When
        boolean execute=sensor.shouldExecuteOnProject(project);
        //Then
        assertTrue("language contains cs, normal mode, should execute",execute);
        
    }

    @Test
    public void notRoot_ShouldNotRun() {
            //Given language is cs
            languages.add("cs");
            when(project.isRoot()).thenReturn(false);
            //When
            boolean execute=sensor.shouldExecuteOnProject(project);
            //Then
            assertFalse("cs, but not root, should not execute",execute);
            
    }
    
    @Test
    public void analyse() {
        File report = new File("report");
        when(inspectCodeRunner.inspectCode()).thenReturn(report);
        
        List<InspectCodeIssue> issues = new ArrayList<InspectCodeIssue>();
        when(inspectCodeResultsParser.parse(report)).thenReturn(issues);
        
        sensor.analyse(project, context);
        
        verify(inspectCodeRunner,times(1)).inspectCode();
        verify(inspectCodeResultsParser,times(1)).parse(report);
        verify(inspectCodeIssuesSaver,times(1)).saveIssues(issues);
    }
}
