package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.utils.AbstractSensorTest;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;

public class VsTestIntegrationTestWorkflowStepsTest extends AbstractSensorTest {

    DefaultPicoContainer container;
    private VsTestIntegrationTestWorkflowSteps steps = new VsTestIntegrationTestWorkflowSteps();
    private WorkflowDirector director = new DefaultDirector(steps);
	@Mock private ResourceResolver resourceResolver;
    
    @Before()
    public void before() {
        container = super.getContainerWithSensorMocks();
		MockitoAnnotations.initMocks(this);
        container
        .addComponent(PathResolver.class)
        .addComponent(resourceResolver);
        
        director.wire(container);
    }
   
    @Test
    public void director_ParserCreation() {
        CoverageReader reader = container.getComponent(CoverageReader.class);
        assertNotNull("create integration test coverage parser",reader);
    }
      
    @Test
    public void OpenCoverDirector_CoverageParserCreation() {
        CoverageReader reader = container.getComponent(CoverageReader.class);
        assertNotNull("create integration test coverage parser",reader);
        assertTrue("should be right class",reader instanceof IntegrationTestCoverageReader);
    }
    
    @Test
    public void director_CoverageSaverCreation() {
        CoverageSaver saver = container.getComponent(CoverageSaver.class);
        assertNotNull("create integration test saver",saver);
        assertTrue("should be right class", saver instanceof DefaultCoverageSaver);
    }
      
}
