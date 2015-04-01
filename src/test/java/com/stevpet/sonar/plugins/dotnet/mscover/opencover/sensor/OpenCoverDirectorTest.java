package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.CoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

public class OpenCoverDirectorTest {

    DefaultPicoContainer container;
    private OpenCoverDirector director = new OpenCoverDirector();
    
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private FileSystemMock fileSystemMock = new FileSystemMock();
    
    @Before()
    public void before() {
        container = new DefaultPicoContainer();
        container.addComponent(msCoverPropertiesMock.getMock())
        .addComponent(VsTestEnvironment.class)
        .addComponent(OpenCoverCommand.class)
        .addComponent(microsoftWindowsEnvironmentMock.getMock())
        .addComponent(fileSystemMock.getMock());
        
        director.wire(container);
    }
   
    @Test
    public void OpenCoverDirector_ParserCreation() {
        CoverageParser parser = container.getComponent(CoverageParser.class);
        assertNotNull("create parser",parser);
    }
    
    @Test
    public void OpenCoverDirector_OpenCoverRunnerCreation() {
        CoverageRunner runner = container.getComponent(OpenCoverCoverageRunner.class);
        assertNotNull("create opencover runner",runner);
    }
    
    @Test
    public void OpenCoverDirector_CoverageParserCreation() {
        CoverageParser parser = container.getComponent(CoverageParser.class);
        assertNotNull("create opencover coverage parser",parser);
        assertTrue("should be right class",parser instanceof OpenCoverCoverageParser);
    }
}
