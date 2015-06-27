package com.stevpet.sonar.plugings.dotnet.resharper;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.sonar.api.batch.Sensor;
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
}
