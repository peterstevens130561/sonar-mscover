package com.stevpet.sonar.plugins.dotnet.resharper;

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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.resharper.DefaultReSharperWorkflow;
import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;
import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeRunner;
import com.stevpet.sonar.plugins.dotnet.resharper.ReSharperConfiguration;
import com.stevpet.sonar.plugins.dotnet.resharper.ReSharperSensor;
import com.stevpet.sonar.plugins.dotnet.resharper.ResharperWorkflow;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.InspectCodeResultsParser;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueValidationException;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueValidator;
import com.stevpet.sonar.plugins.dotnet.resharper.saver.InspectCodeIssuesSaver;


public class ReSharperSensorTest {

    Sensor sensor ;
    @Mock private FileSystem fileSystem;
    @Mock private Settings settings;
    @Mock private InspectCodeResultsParser inspectCodeResultsParser;
    @Mock private InspectCodeIssuesSaver inspectCodeIssuesSaver;
    @Mock private InspectCodeRunner inspectCodeRunner;
    @Mock private Project project;
    @Mock private SensorContext context;
    @Mock private ReSharperConfiguration resharperConfiguration;
    @Mock private IssueValidator issueValidator;
    private SortedSet<String> languages;
    private ResharperWorkflow reSharperWorkflow;
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        reSharperWorkflow = new DefaultReSharperWorkflow(inspectCodeResultsParser, inspectCodeIssuesSaver, inspectCodeRunner,issueValidator);
        sensor = new ReSharperSensor(fileSystem, settings, reSharperWorkflow ,resharperConfiguration);
        languages = new TreeSet<String>();
        when(fileSystem.languages()).thenReturn(languages);
        when(project.isRoot()).thenReturn(false);
        when(resharperConfiguration.failOnException()).thenReturn(true);
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
        when(settings.getString(ReSharperConfiguration.MODE)).thenReturn(ReSharperConfiguration.MODE_SKIP);
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
        when(settings.getString(ReSharperConfiguration.MODE)).thenReturn("");
        //When
        boolean execute=sensor.shouldExecuteOnProject(project);
        //Then
        assertTrue("language contains cs, normal mode, should execute",execute);
        
    }

    @Test
    public void notChild_ShouldNotRun() {
            //Given language is cs
            languages.add("cs");
            when(project.isRoot()).thenReturn(true);
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
        verify(inspectCodeIssuesSaver,times(1)).saveModuleIssues(issues,project);
        verify(issueValidator,times(1)).validate(issues);
    }
    
    
    @Test
    public void analyse_Failing() {
        File report = new File("report");
        when(inspectCodeRunner.inspectCode()).thenReturn(report);
        InspectCodeIssue issue = new InspectCodeIssue();
        List<InspectCodeIssue> issues = new ArrayList<InspectCodeIssue>();
        when(inspectCodeResultsParser.parse(report)).thenReturn(issues);
        when(issueValidator.validationFailed()).thenReturn(true,true,true);
        when(issueValidator.getException()).thenReturn(new IssueValidationException(issue));
        
        boolean thrown=false;
        try {
        sensor.analyse(project, context);
        } catch ( IssueValidationException e) {
            thrown=true;
        }
        assertTrue("IssueValidationException must have been thrown",thrown);
        verify(inspectCodeRunner,times(1)).inspectCode();
        verify(inspectCodeResultsParser,times(1)).parse(report);
        verify(inspectCodeIssuesSaver,times(0)).saveModuleIssues(issues,project);
        verify(issueValidator,times(1)).validate(issues);
    }
    @Test
    public void throwException() {
        when(resharperConfiguration.failOnException()).thenReturn(true);
        when(inspectCodeRunner.inspectCode()).thenThrow(new SonarException("rethrow me"));
        try {
            sensor.analyse(project, context);
        } catch (SonarException e) {
            return;
        }
        fail("should have thrown exception, as set to fail");
    }
    

    @Test
    public void catchException() {
        when(resharperConfiguration.failOnException()).thenReturn(false);
        when(inspectCodeRunner.inspectCode()).thenThrow(new SonarException("do not rethrow me"));
        try {
            sensor.analyse(project, context);
        } catch (SonarException e) {
            fail("should not have rethrown exception");
        }
        verify(inspectCodeResultsParser,times(0)).parse(any(File.class));
        verify(inspectCodeIssuesSaver,times(0)).saveIssues(anyList());
    }
}
