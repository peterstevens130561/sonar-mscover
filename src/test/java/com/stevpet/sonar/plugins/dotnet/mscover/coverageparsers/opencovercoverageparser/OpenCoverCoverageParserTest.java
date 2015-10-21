package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class OpenCoverCoverageParserTest {

    @Mock private MsCoverConfiguration configuration;

    @Before 
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }
    

    @Ignore
    @Test
    public void ReadFile() {
        //Arrange
        OpenCoverCoverageParser parser = new OpenCoverCoverageParser(configuration);
        //File file = new File("E:\\Users\\stevpet\\My Documents\\BAM\\coverage-report.xml");
        File file = new File("E:\\Users\\stevpet\\My Documents\\GitHub\\sonar-mscover\\coverage-subsurface.xml");
        SonarCoverage registry = new SonarCoverage();
        //Act
        parser.parse(registry, file);
        //Assert
        assertEquals(356,registry.size());
        assertNull(registry.getCoveredFile("5").getAbsolutePath()); // is not included
        assertNotNull(registry.getCoveredFile("86").getAbsolutePath()); // is included
    }
}
