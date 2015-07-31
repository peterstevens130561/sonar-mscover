package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.TestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.VsTestWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class VsTestWorkflowStepsTest {

    private WorkflowSteps workflow = new VsTestWorkflowSteps();
    private DefaultPicoContainer picoContainer = new DefaultPicoContainer();
   
    @Mock FileSystem fileSystem;
    @Mock MsCoverConfiguration msCoverProperties;
    @Mock SensorContext sensorContext;
    @Mock Project project;
    @Mock PathResolver pathResolver;
    @Mock VsTestEnvironment vsTestEnvironment;
    @Mock MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
       workflow.getComponents(picoContainer);
       picoContainer.addComponent(vsTestEnvironment);
       picoContainer.addComponent(fileSystem)
       .addComponent(microsoftWindowsEnvironment)
       .addComponent(DefaultResourceResolver.class)
       .addComponent(msCoverProperties)
       .addComponent(sensorContext)
       .addComponent(pathResolver)
       .addComponent(project);
    }
    @Test
    public void createCoverageReader() {
        picoContainer.addComponent(workflow.getCoverageReader());
        CoverageReader reader=picoContainer.getComponent(CoverageReader.class);
        assertNotNull("Could not create CoverageReader",reader);
    }
    
    @Test
    public void createTestRunner() {
        picoContainer.addComponent(workflow.getTestRunner());
        TestRunner runner=picoContainer.getComponent(TestRunner.class);
        assertNotNull("Could not create TestRunner",runner);
    }
    
    @Test
    public void createTestResultsBuilder() {
        picoContainer.addComponent(workflow.getTestResultsBuilder());
        TestResultsBuilder resultsbuilder=picoContainer.getComponent(TestResultsBuilder.class);
        assertNotNull("Could not create TestResultsBuilder",resultsbuilder);
    }
    
    @Test
    public void createTestResultsSaver() {
        picoContainer.addComponent(workflow.getTestResultsSaver());
        TestResultsSaver resultsSaver=picoContainer.getComponent(TestResultsSaver.class);
        assertNotNull("Could not create TestResultsSaver",resultsSaver);
    }
    
    @Test
    public void createCoverageSaver() {
        picoContainer.addComponent(workflow.getCoverageSaver());
        CoverageSaver coverageSaver=picoContainer.getComponent(CoverageSaver.class);
        assertNotNull("Could not create TestResultsSaver",coverageSaver);        
    }
  
}
